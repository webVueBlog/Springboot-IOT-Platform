package com.webVueBlog.iot.tdengine.service.factory;

import com.webVueBlog.framework.config.MyBatisConfig;
import com.webVueBlog.iot.tdengine.service.impl.MySqlLogServiceImpl;
import com.webVueBlog.iot.tdengine.service.impl.TdengineLogServiceImpl;
import com.webVueBlog.iot.tdengine.config.TDengineConfig;
import com.webVueBlog.iot.tdengine.service.ILogService;
import com.webVueBlog.iot.mapper.DeviceLogMapper;
import com.webVueBlog.iot.tdengine.dao.TDDeviceLogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 类名: DeviceLogServiceImpl
 */
@Component
public class LogServiceFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ILogService getLogService() {
        //先获取TDengine的配置，检测TDengine是否已经配置
        if (containBean(TDengineConfig.class)) {
            TDengineConfig tDengineConfig = applicationContext.getBean(TDengineConfig.class);
            TDDeviceLogDAO tDDeviceLogDAO = applicationContext.getBean(TDDeviceLogDAO.class);
            ILogService logService = new TdengineLogServiceImpl(tDengineConfig, tDDeviceLogDAO);
            return logService;
        } else if (containBean(MyBatisConfig.class)) {
            //没有配置TDengine，那么使用MySQL的日志配置
            DeviceLogMapper deviceLogMapper = applicationContext.getBean( DeviceLogMapper.class);
            ILogService logService = new MySqlLogServiceImpl(deviceLogMapper);
            return logService;
        } else {
            return null;
        }
    }

    /**
    * @Method containBean
    * @Description 根据类判断是否有对应bean
    * @Param 类
    * @return
    *
    *
    */
    private boolean containBean(@Nullable Class<?> T) {
        String[] beans = applicationContext.getBeanNamesForType(T);
        if (beans == null || beans.length == 0) {
            return false;
        } else {
            return true;
        }
    }
}
