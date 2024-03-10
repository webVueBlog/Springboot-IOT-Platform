package com.webVueBlog.mqtt.server;

import com.webVueBlog.server.Server;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.mqtt.handler.adapter.MqttMessageAdapter;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MqttServer extends Server {

    @Autowired
    private MqttMessageAdapter messageAdapter;// 消息适配器

    @Override
    protected AbstractBootstrap initialize() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory(config.name, Thread.MAX_PRIORITY));// 1 boss线程
        workerGroup = new NioEventLoopGroup(config.workerCore, new DefaultThreadFactory(config.name, Thread.MAX_PRIORITY));// 工作线程

        if (config.businessCore > 0) {
            businessService = new ThreadPoolExecutor(config.businessCore, config.businessCore, 1L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new DefaultThreadFactory(config.name, true, Thread.NORM_PRIORITY));
        }
        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .option(ChannelOption.SO_BACKLOG, 511)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        //客户端心跳检测机制
                        channel.pipeline()
                                .addFirst(DaConstant.SERVER.IDLE
                                        , new IdleStateHandler(config.readerIdleTime, config.writerIdleTime, config.allIdleTime, TimeUnit.SECONDS))
                                .addLast(DaConstant.SERVER.DECODER, new MqttDecoder(1024 * 1024 * 2))
                                .addLast(DaConstant.SERVER.ENCODER, MqttEncoder.INSTANCE)
                                .addLast(messageAdapter);
                    }
                });


    }
}
