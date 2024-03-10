package com.webVueBlog.server.handler;

import com.webVueBlog.base.codec.Delimiter;
import com.webVueBlog.base.session.Packet;
import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.session.SessionManager;
import com.webVueBlog.base.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;


import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * UDP消息适配器
 *
 * 
 */
@ChannelHandler.Sharable
@Slf4j
public class UDPMessageAdapter extends ChannelInboundHandlerAdapter {

    private final SessionManager sessionManager;

    private final long readerIdleTime;

    public static UDPMessageAdapter newInstance(SessionManager sessionManager,
                                                int readerIdleTime, Delimiter[] delimiters) {
        return new DelimiterBaseFrame(sessionManager, readerIdleTime, delimiters);
    }

    public UDPMessageAdapter(SessionManager sessionManager, int readerIdleTime) {
        this.sessionManager = sessionManager;
        this.readerIdleTime = TimeUnit.DAYS.toMillis(readerIdleTime);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf buf = packet.content();
        /*session处理*/
        Session session = getSession(ctx, packet.sender());
        /*更新最后上线时间*/
        session.access();
        /*将报文透传到下一个处理器*/
        ctx.fireChannelRead(Packet.of(session, buf));
    }

    /*TODO tcp跟 udp的session 暂时分开处理*/
    private final Map<Object, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 设备注册
     */
    protected Session getSession(ChannelHandlerContext ctx, InetSocketAddress sender) {
        Session session = sessionMap.get(sender);
        if (session == null) {
            session = sessionManager.newInstance(ctx.channel(), sender, s -> sessionMap.remove(sender, s));
            sessionMap.put(sender, session);
            log.info("=>UDP设备连接{}", session);
        }
        return session;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /*根据UDP设备设定的心跳频率判断是否在线*/
        Thread thread = new Thread(() -> {
            for (; ; ) {
                long nextDelay = readerIdleTime;
                long now = System.currentTimeMillis();

                for (Session session : sessionMap.values()) {
                    /*readerIdleTime 读心跳时间 根据设备的特性设置*/
                    long time = readerIdleTime - (now - session.getLastAccessTime());

                    if (time <= 0) {
                        log.warn("=>设备心跳超时 {}", session);
                        session.invalidate();
                    } else {
                        nextDelay = Math.min(time, nextDelay);
                    }
                }
                try {
                    Thread.sleep(nextDelay);
                } catch (Throwable e) {
                    log.warn("IdleStateScheduler", e);
                }
            }
        });
        thread.setName(Thread.currentThread().getName() + "-c");
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 分隔符报文处理器
     */
    private static class DelimiterBaseFrame extends UDPMessageAdapter {

        private final Delimiter[] delimiters;

        public DelimiterBaseFrame(SessionManager sessionManager, int readerIdleTime, Delimiter[] delimiters) {
            super(sessionManager, readerIdleTime);
            this.delimiters = delimiters;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            DatagramPacket packet = (DatagramPacket) msg;
            ByteBuf content = packet.content();
            Session session = getSession(ctx, packet.sender());

            try {
                /*根据长度 分隔符过滤不符合报文*/
                List<ByteBuf> out = decode(content);
                for (ByteBuf byteBuf : out) {
                    /*透传到下一个处理器*/
                    ctx.fireChannelRead(Packet.of(session, byteBuf));
                }
            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                /*最后释放缓存*/
                content.release();
            }
        }

        protected List<ByteBuf> decode(ByteBuf in) {
            List<ByteBuf> out = new LinkedList<>();
            while (in.isReadable()) {

                for (Delimiter delimiter : delimiters) {
                    int length = delimiter.value.length;
                    int frameLength = ByteBufUtils.indexOf(in, delimiter.value);
                    if (frameLength >= 0) {
                        if (delimiter.strip) {
                            if (frameLength != 0) {
                                out.add(in.readRetainedSlice(frameLength));
                            }
                            in.skipBytes(length);
                        } else {
                            if (frameLength != 0) {
                                out.add(in.readRetainedSlice(frameLength + length));
                            } else {
                                in.skipBytes(length);
                            }
                        }
                    } else {
                        int i = in.readableBytes();
                        if (i > 0) {
                            out.add(in.readRetainedSlice(i));
                        }
                    }
                }
            }
            return out;
        }
    }


}
