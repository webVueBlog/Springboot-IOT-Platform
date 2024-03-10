package com.webVueBlog.server;


import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.server.config.NettyConfig;
import com.webVueBlog.server.handler.*;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TCP服务
 * 粘包处理为 分隔符和固定长度
 * 需要其他方式处理粘包，按照流程添加
 *
 * 
 */
public class TCPServer extends Server {

    public TCPServer(NettyConfig config) {
        super(config);
    }

    @Override
    protected AbstractBootstrap initialize() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory(config.name, Thread.MAX_PRIORITY));
        workerGroup = new NioEventLoopGroup(config.workerCore, new DefaultThreadFactory(config.name, Thread.MAX_PRIORITY));
        if (config.businessCore > 0) {
            businessService = new ThreadPoolExecutor(config.businessCore, config.businessCore, 1L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new DefaultThreadFactory(config.name + "-B", true, Thread.NORM_PRIORITY));
        }
        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(NioChannelOption.SO_REUSEADDR, true)
                .option(NioChannelOption.SO_BACKLOG, 1024)
                .childOption(NioChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    /*第二个处理器 session处理*/
                    private final TCPMessageAdapter adapter = new TCPMessageAdapter(config.sessionManager);
                    /*3.解码 适配解码器 解码后业务处理*/
                    private final MessageDecoderWrapper decoder = new MessageDecoderWrapper(config.decoder);
                    /*3.编码 适配编码器-编码后业务处理*/
                    private final MessageEncoderWrapper encoder = new MessageEncoderWrapper(config.encoder);
                    /*4.编解码后消息分发器 同步和异步处理*/
                    private final DispatcherHandler dispatcher = new DispatcherHandler(config.handlerMapping, config.handlerInterceptor, bossGroup);

                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new IdleStateHandler(config.readerIdleTime, config.writerIdleTime, config.allIdleTime)) //设置心跳时间
                                // .addLast(DaConstant.SERVER.FRAMEDECODER, frameDecoder())//粘包处理器
                                .addLast(DaConstant.SERVER.ADAPTER, adapter)//消息适配器
                                .addLast(DaConstant.SERVER.DECODER, decoder) //报文解码器
                                .addLast(DaConstant.SERVER.ENCODER, encoder) //报文编码器
                                .addLast(DaConstant.SERVER.DISPATCHER, dispatcher); //消息分发
                    }
                });
    }

    /**
     * 添加TCP粘包处理器
     */
    private ByteToMessageDecoder frameDecoder() {
        if (config.lengthField == null) {
            /*分隔符处理器，报文以固定包头包尾结束*/
            return new DelimiterBasedFrameDecoder(config.maxFrameLength, config.delimiters);
        }
        /*报文长度的，以长度固定处理器和分隔符处理器 处理*/
        return new LengthFieldAndDelimiterFrameDecoder(config.maxFrameLength, config.lengthField, config.delimiters);
    }
}
