package com.webVueBlog.common.config;

import com.webVueBlog.common.constant.DaConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 设备报文处理线程池
 */
@Configuration//@Configuration注解的作用就是标记配置类，兼备Component的效果。
@EnableAsync//@EnableAsync注解的作用就是开启异步任务的支持，这样我们就可以在业务逻辑中使用@Async注解来进行异步调用了。
@ConfigurationProperties(prefix = "spring.task.execution.pool")//读取application.yml中的配置信息并与实体类映射。
@Data//lombok注解，自动生成getter和setter方法
public class DeviceTask {

    private int coreSize;//核心线程数

    private int maxSize;//最大线程数

    private int queueCapacity;//队列最大长度

    private int  keepAlive;//keepAlive是当线程数大于核心线程数时，多余的空闲线程存活的最长时间


    /*设备状态池*/
    @Bean(DaConstant.TASK.DEVICE_STATUS_TASK)/*bean的名称，也就是方法名 设备上下线任务*/
    public Executor deviceStatusTaskExecutor() {//Executor是Spring的异步执行接口，ThreadPoolTaskExecutor 是Spring对线程池的实现
      return builder(DaConstant.TASK.DEVICE_STATUS_TASK);//返回线程池实例
    }

    /*平台自动获取线程池(例如定时获取设备信息)*/
    @Bean(DaConstant.TASK.DEVICE_FETCH_PROP_TASK)
    public Executor deviceFetchTaskExecutor() {
        return builder(DaConstant.TASK.DEVICE_FETCH_PROP_TASK);//属性读取任务,区分服务调用
    }

    //@Bean是一个方法级别的注解，主要用在@Configuration注解的类里，也可以用在@Component注解的类里。
    //@Bean注解的作用就是注册bean对象，然后spring容器可以管理这个bean对象，在需要的时候注入到其他类中。
    /*设备回调信息(下发指令(服务)设备应答信息)*/
    @Bean(DaConstant.TASK.DEVICE_REPLY_MESSAGE_TASK)
    public Executor deviceReplyTaskExecutor() {
        return builder(DaConstant.TASK.DEVICE_REPLY_MESSAGE_TASK);// 设备回调任务
    }

    /*设备主动上报(设备数据有变化主动上报)*/
    @Bean(DaConstant.TASK.DEVICE_UP_MESSAGE_TASK)
    public Executor deviceUpMessageTaskExecutor() {
        return builder(DaConstant.TASK.DEVICE_UP_MESSAGE_TASK);//设备主动上报任务
    }

    /*指令下发(服务下发)*/
    @Bean(DaConstant.TASK.FUNCTION_INVOKE_TASK)
    public Executor functionInvokeTaskExecutor() {
        return builder(DaConstant.TASK.FUNCTION_INVOKE_TASK);//服务调用(指令下发)任务
    }

    /*内部消费线程*/
    @Bean(DaConstant.TASK.MESSAGE_CONSUME_TASK)
    public Executor messageConsumeTaskExecutor() {
        return builder(DaConstant.TASK.MESSAGE_CONSUME_TASK);//消息消费线程
    }

    @Bean(DaConstant.TASK.MESSAGE_CONSUME_TASK_PUB)
    public Executor messageConsumePubTaskExecutor(){
        return builder(DaConstant.TASK.MESSAGE_CONSUME_TASK_PUB);//内部消费线程publish
    }

    @Bean(DaConstant.TASK.MESSAGE_CONSUME_TASK_FETCH)
    public Executor messageConsumeFetchTaskExecutor(){
        return builder(DaConstant.TASK.MESSAGE_CONSUME_TASK_FETCH);//内部消费线程Fetch
    }

    @Bean(DaConstant.TASK.DELAY_UPGRADE_TASK)
    public Executor delayedTaskExecutor(){
        return builder(DaConstant.TASK.DELAY_UPGRADE_TASK);//OTA升级延迟队列
    }

    /*设备其他消息处理*/
    @Bean(DaConstant.TASK.DEVICE_OTHER_TASK)
    public Executor deviceOtherTaskExecutor(){
        return builder(DaConstant.TASK.DEVICE_OTHER_TASK);//设备其他消息处理
    }

    /*组装线程池*/

    /**
     * threadNamePrefix是线程池的名字
     * @param threadNamePrefix是线程池的名字
     * @return ThreadPoolTaskExecutor是线程池
     */
    private ThreadPoolTaskExecutor builder(String threadNamePrefix){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();//创建线程池
        executor.setCorePoolSize(coreSize);//核心线程数
        executor.setMaxPoolSize(maxSize);//最大线程数
        executor.setKeepAliveSeconds(keepAlive);//setKeepAliveSeconds是线程空闲后的最大存活时间
        executor.setQueueCapacity(queueCapacity);//队列容量
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());//丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
        //线程池名的前缀
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();//初始化线程池
        return executor;//返回线程池
    }

}
