package com.webVueBlog.mqtt.server;

import com.webVueBlog.server.Server;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.mqtt.codec.WebSocketMqttCodec;
import com.webVueBlog.mqtt.handler.adapter.MqttMessageAdapter;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
@Slf4j
public class WebSocketServer extends Server {

    @Autowired
    private WebSocketMqttCodec webSocketMqttCodec;
    @Autowired
    private MqttMessageAdapter mqttMessageAdapter;


    @Override
    protected AbstractBootstrap initialize() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addFirst(DaConstant.WS.HEART_BEAT
                                        , new IdleStateHandler(0, 0, 70))
                                /*http请求响应*/
                                .addLast(DaConstant.WS.HTTP_SERVER_CODEC, new HttpServerCodec())
                                /*聚合header与body组成完整的Http请求，最大数据量为1Mb*/
                                .addLast(DaConstant.WS.AGGREGATOR, new HttpObjectAggregator(1024 * 1024))
                                /*压缩出站数据*/
                                .addLast(DaConstant.WS.COMPRESSOR, new HttpContentCompressor())
                                /*WebSocket协议配置mqtt*/
                                .addLast(DaConstant.WS.PROTOCOL, new WebSocketServerProtocolHandler("/mqtt",
                                        "mqtt,mqttv3.1,mqttv3.1.1,mqttv5.0", true, 65536))
                                .addLast(DaConstant.WS.MQTT_WEBSOCKET, webSocketMqttCodec)
                                .addLast(DaConstant.WS.DECODER, new MqttDecoder())
                                .addLast(DaConstant.WS.ENCODER, MqttEncoder.INSTANCE)
                                .addLast(DaConstant.WS.BROKER_HANDLER, mqttMessageAdapter);
                    }
                });
    }
}
