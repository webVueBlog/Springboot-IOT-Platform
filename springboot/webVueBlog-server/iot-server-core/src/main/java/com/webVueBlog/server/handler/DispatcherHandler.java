package com.webVueBlog.server.handler;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.base.core.HandlerInterceptor;
import com.webVueBlog.base.core.HandlerMapping;
import com.webVueBlog.base.core.hanler.BaseHandler;
import com.webVueBlog.base.session.Packet;
import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.util.Stopwatch;
import com.webVueBlog.common.core.thingsModel.ThingsModelSimpleItem;
import com.webVueBlog.common.enums.ServerType;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.mq.redischannel.producer.MessageProducer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 消息分发处理
 * 
 */
@Slf4j
@ChannelHandler.Sharable
public class DispatcherHandler extends ChannelInboundHandlerAdapter {

    private final HandlerMapping handlerMapping;

    private final HandlerInterceptor interceptor;

    private final ExecutorService executor;

    public static boolean STOPWATCH = false;

    private static Stopwatch s;

    public DispatcherHandler(HandlerMapping handlerMapping, HandlerInterceptor interceptor, ExecutorService executor) {
        if (STOPWATCH && s == null) {
            s = new Stopwatch().start();
        }
        this.handlerMapping = handlerMapping;
        this.interceptor = interceptor;
        this.executor = executor;
    }

    /**
     * TCP-4 消息处理
     * @param ctx
     * @param msg
     */
    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (STOPWATCH) {
            s.increment();
        }

        Packet packet = (Packet) msg;
        Message request = packet.message;
        /**
         * TCP的数据包四种情况
         *   messageId用于标记上传的包数据的标志位的值，isPackage：表示是否整个包上传
         * 1. 整包上传: 包含：设备编号(注册包)，数据包(心跳包) -->处理注册上传 和 上报数据
         * 2. 单个注册包，或者心跳包上传，有标识位 -> 如0x80,表示注册包,只处理注册上传
         * 3. 单个数据包上传，有标识位 ，如 0x89 -->表示xxxx 解码数据包转发到MQ
         * 4. 单个数据包上传，无标识位 ，如 modbus  --> 安装约定的协议处理
         */
        // 4.单个数据包上传无标识位
        if (null == request.getMessageId()){
           this.handleReport(request);
        }
        //1. 整包上传: 包含：设备编号(注册包)，数据包(心跳包) -->处理注册上传 和 上报数据
        if (request.getIsPackage()){
            //获取设备编号
            List<ThingsModelSimpleItem> items = ((DeviceReport) request).getValuesInput().getThingsModelValueRemarkItem();
            for (ThingsModelSimpleItem item : items) {
                if (null !=item.getId() && item.getId().equals("dev_id")){
                    request.setClientId(item.getValue());
                }else if (null != item.getId() && item.getId().equals("imei")){
                    request.setClientId(item.getValue());
                }else if (null != item.getId() && item.getId().equals("id")){
                    request.setClientId(item.getValue());
                }else if (null != item.getId() && item.getId().equals("device")){
                    request.setClientId(item.getValue());
                }
            }
            if (null != request.getClientId()){
                //先处理设备注册
                this.handleMessage(request,packet,ctx);
            }
            //处理消息转发
            this.handleReport(request);

            // 2/3 单个注册包，心跳，数据包上传，匹配消息id处理
        }else if (null != request.getMessageId() && !"0".equals(request.getMessageId())){
            //处理心跳和设备注册
            this.handleMessage(request,packet,ctx);
        }
    }

    private void handleMessage(Message message,Packet packet,ChannelHandlerContext ctx){
        /*获取消息的处理方法 根据注解 @PakMapping 匹配方法*/
        BaseHandler handler = handlerMapping.getHandler(Integer.parseInt(message.getMessageId()));

        if (handler == null) {
            Message response = interceptor.notSupported(message, packet.session);
            if (response != null) {
                ctx.writeAndFlush(packet.replace(response));
            }
        } else {
            if (handler.async) {
                executor.execute(() -> channelRead0(ctx, packet, handler));
            } else {
                channelRead0(ctx, packet, handler);
            }
        }
    }
    private void handleReport(Message request){
        DeviceReport report = (DeviceReport)request;
        DeviceReportBo reportBo = new DeviceReportBo();
        reportBo.setValuesInput(report.getValuesInput());
        reportBo.setPlatformDate(DateUtils.getNowDate());
        reportBo.setSerialNumber(report.getClientId());
        reportBo.setServerType(ServerType.TCP);
        reportBo.setSlaveId(report.getSlaveId());
        reportBo.setReplyMessage(report.getReplyMessage());
        reportBo.setIsReply(report.getIsReply());
        reportBo.setAddress(report.getAddress());
        reportBo.setStatus(report.getStatus());
        reportBo.setProtocolCode(DaConstant.PROTOCOL.ModbusRtu);
        MessageProducer.sendPublishMsg(reportBo);
        return;
    }

    private void channelRead0(ChannelHandlerContext ctx, Packet packet, BaseHandler handler) {
        Session session = packet.session;
        Message request = packet.message;
        Message response;
        long time = System.currentTimeMillis();

        try {
            //判断是否是前置拦截的消息类型，若不是则不处理
            if (!interceptor.beforeHandle(request, session)) {

            }
            /*调用@PakMapping注解标注方法执行*/
            response = handler.invoke(request, session);
            //无返回值处理
            if (handler.returnVoid) {
                response = interceptor.successful(request, session);
            } else {
                //有返回值，进行AOP下一一个方法
                interceptor.afterHandle(request, response, session);
            }
        } catch (Exception e) {
            log.warn(String.valueOf(request), e);
            response = interceptor.exceptional(request, session, e);
        }
        time = System.currentTimeMillis() - time;
        if (time > 100) {
            log.info("={},耗时{}ms", handler, time);
        }
        //有返回值，则应答设备
        if (response != null) {
            ctx.writeAndFlush(packet.replace(response));
        }
    }
}
