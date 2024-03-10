package com.webVueBlog.mqtt.auth;

import com.webVueBlog.common.constant.Constants;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.iot.model.MqttAuthenticationModel;
import com.webVueBlog.iot.service.IToolService;
import com.webVueBlog.mq.mqttClient.MqttClientConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 客户端认证
 *
 * 
 */
@Slf4j
@Component
public class AuthService {

    @Resource
    private IToolService toolService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private MqttClientConfig mqttConfig;
    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    /**
     * MQTT客户端认证
     *
     * @param clientId 客户端id
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public boolean auth(String clientId, String username, String password) {
        /*认证次数*/
        redisCache.incr2(DaConstant.REDIS.MESSAGE_AUTH_TOTAL, -1L);
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            log.error("=>客户端参数缺少,clientId:{},username:{},password:{}", clientId, username, password);
            return false;
        }
        try {
            if (clientId.startsWith("server")) {
                // 服务端认证：配置的账号密码认证
                if (mqttConfig.getUsername().equals(username) && mqttConfig.getPassword().equals(password)) {
                    log.info("-----------服务端mqtt认证成功,clientId:" + clientId + "---------------");
                    return true;
                } else {
                    ResponseEntity response = toolService.returnUnauthorized(new MqttAuthenticationModel(clientId, username, password), "mqtt账号和密码与认证服务器配置不匹配");
                    log.warn("=>服务端认证失败[{}]", response.getBody());
                    throw new ServiceException("服务端认证失败:"+response.getBody());
                }
            } else if (clientId.startsWith("web") || clientId.startsWith("phone")) {
                // web端和移动端认证：token认证
                String token = password;
                if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
                    token = token.replace(Constants.TOKEN_PREFIX, "");
                }
                try {
                    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                    log.info("-----------移动端/Web端mqtt认证成功,clientId:" + clientId + "---------------");
                    return true;
                } catch (Exception ex) {
                    ResponseEntity response = toolService.returnUnauthorized(new MqttAuthenticationModel(clientId, username, password), ex.getMessage());
                    log.warn("=>移动端/Web端mqtt认证失败[{}]",response.getBody());
                    throw new ServiceException("移动端/Web端mqtt认证失败:"+response.getBody());
                }
            } else {
                // 设备端认证
                ResponseEntity response = toolService.clientAuth(clientId, username, password);
                if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
                    return true;
                } else {
                    log.warn("=>设备端认证失败");
                    throw new ServiceException("设备端认证失败:"+response.getBody());
                }
            }
        } catch (Exception e) {
            log.error("=>客户端认证失败", e);
            return false;
        }
    }


}
