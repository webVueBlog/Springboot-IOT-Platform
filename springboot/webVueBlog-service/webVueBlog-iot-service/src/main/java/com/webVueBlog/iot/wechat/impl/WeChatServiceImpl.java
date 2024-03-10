package com.webVueBlog.iot.wechat.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.common.core.domain.entity.SysUser;
import com.webVueBlog.common.core.domain.model.LoginUser;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.enums.SocialPlatformType;
import com.webVueBlog.common.enums.VerifyTypeEnum;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.common.utils.bean.BeanUtils;
import com.webVueBlog.common.utils.http.HttpUtils;
import com.webVueBlog.common.utils.uuid.IdUtils;
import com.webVueBlog.common.wechat.WeChatAppResult;
import com.webVueBlog.common.wechat.WeChatLoginBody;
import com.webVueBlog.common.wechat.WeChatLoginResult;
import com.webVueBlog.common.wechat.WeChatMiniProgramResult;
import com.webVueBlog.common.wechat.WeChatPhoneInfo;
import com.webVueBlog.common.wechat.WeChatUserInfo;
import com.webVueBlog.framework.web.service.SysLoginService;
import com.webVueBlog.iot.domain.SocialPlatform;
import com.webVueBlog.iot.domain.SocialUser;
import com.webVueBlog.iot.model.RegisterUserInput;
import com.webVueBlog.iot.model.RegisterUserOutput;
import com.webVueBlog.iot.model.login.WeChatLoginQrRes;
import com.webVueBlog.iot.service.ISocialLoginService;
import com.webVueBlog.iot.service.ISocialPlatformService;
import com.webVueBlog.iot.service.ISocialUserService;
import com.webVueBlog.iot.service.IToolService;
import com.webVueBlog.iot.wechat.WeChatService;
import com.webVueBlog.iot.wechat.vo.WxBindReqVO;
import com.webVueBlog.iot.wechat.vo.WxCancelBindReqVO;
import com.webVueBlog.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.webVueBlog.common.core.domain.AjaxResult.error;
import static com.webVueBlog.common.core.domain.AjaxResult.success;
import static com.webVueBlog.common.utils.SecurityUtils.getLoginUser;
import static com.webVueBlog.common.utils.SecurityUtils.getUserId;
import static com.webVueBlog.iot.service.impl.SocialLoginServiceImpl.LOGIN_ERROR_MSG_REDIS_KEY;
import static com.webVueBlog.iot.service.impl.SocialLoginServiceImpl.WX_BIND_REDIS_KEY;

/**
 * WeChatServiceImpl是微信登录的实现类
 */
@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {

    @Resource
    private ISocialUserService socialUserService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private SysLoginService sysLoginService;
    @Resource
    private ISocialPlatformService socialPlatformService;
    @Resource
    private ISocialLoginService socialLoginService;
    @Resource
    private IToolService toolService;
    @Resource
    private RedisCache redisCache;

    /**
     * 移动应用登录获取用户access_token
     */
    private static final String WE_CHAT_APP_API_CODE_TO_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 小程序登录获取用户会话参数
     */
    private static final String WE_CHAT_MINI_PROGRAM_API_CODE_TO_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信小程序获取access_token
     */
    private static final String WE_CHAT_MINI_PROGRAM_API_TO_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 获取用户信息
     */
    private static final String WE_CHAT_API_TO_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 获取用户手机号信息
     */
    private static final String WE_CHAT_API_TO_USER_PHONE_INFO_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";

    /**
     * 移动应用微信登录
     * @param weChatLoginBody 微信登录参数
     * @return String
     */
    public WeChatLoginResult mobileLogin(WeChatLoginBody weChatLoginBody) {
        WeChatLoginResult weChatLoginResult = new WeChatLoginResult();// 返回结果
        SocialPlatformType socialPlatformType = SocialPlatformType.WECHAT_OPEN_MOBILE;// 社交平台类型
        // 查询微信平台信息
        SocialPlatform socialPlatform = socialPlatformService.selectSocialPlatformByPlatform(socialPlatformType.getSourceClient());// 获取微信开放平台移动应用信息
        if (socialPlatform == null) {
            throw new ServiceException("请先配置微信开放平台移动应用信息");
        }
        // 用户凭证code只能消费一次，前端调的uni.login有时会消费，然后直接就把获取到的信息传过来，不会消费的话就这里通过code拿
        if (StringUtils.isEmpty(weChatLoginBody.getAccessToken()) || StringUtils.isEmpty(weChatLoginBody.getOpenId()) || StringUtils.isEmpty(weChatLoginBody.getUnionId())) {// 用户凭证code
            WeChatAppResult weChatResult = this.getAccessTokenOpenId(weChatLoginBody.getCode(), socialPlatform);// 获取用户凭证
            if (weChatResult == null || weChatResult.getErrCode() != null) {// 凭证获取失败
                throw new ServiceException("用户凭证获取失败，请重新登录！");// 用户凭证code只能消费一次，前端调的uni.login有时会消费，然后直接就把获取到的信息传过来，不会消费的话就
            }
            weChatLoginBody.setAccessToken(weChatResult.getAccessToken()).setRefreshToken(weChatResult.getRefreshToken()).setExpiresIn(weChatResult.getExpiresIn()).setOpenId(weChatResult.getOpenId()).setUnionId(weChatResult.getUnionId()).setScope(weChatResult.getScope());
            // 用户凭证code只能消费一次，前端调的uni.login有时会消费，然后直接就把获取到的信息传过来，不会消费的话就
        }
        Long bindSysUserId;// 绑定系统用户ID
        // 查询用户第三方信息
        SocialUser socialUser = socialUserService.selectOneByOpenIdAndUnionId(weChatLoginBody.getOpenId(), weChatLoginBody.getUnionId());// 查询用户第三方信息
        if (socialUser != null && socialUser.getSysUserId() != null) {// 用户已绑定系统用户ID
            bindSysUserId = socialUser.getSysUserId();// 用户已绑定系统用户ID
        } else {
            bindSysUserId = socialUserService.selectSysUserIdByUnionId(weChatLoginBody.getUnionId());// 查询用户系统用户ID
        }
        Date nowDate = DateUtils.getNowDate();// 当前时间
        String uuid = IdUtils.randomUUID();// 生成UUID
        if (socialUser == null) {// 用户未绑定系统用户ID
            SocialUser addSocialUser = new SocialUser();// 新增用户第三方信息
            addSocialUser.setSysUserId(bindSysUserId).setStatus(bindSysUserId == null ? "0" : "1").setUuid(uuid).setSource(socialPlatform.getPlatform().toUpperCase(Locale.ROOT)).setAccessToken(weChatLoginBody.getAccessToken()).setExpireIn(weChatLoginBody.getExpiresIn())
                    .setRefreshToken(weChatLoginBody.getRefreshToken()).setOpenId(weChatLoginBody.getOpenId()).setUnionId(weChatLoginBody.getUnionId()).setSourceClient(socialPlatform.getPlatform())
                    .setCode(weChatLoginBody.getCode());// 第三方登录授权码
            addSocialUser.setCreateBy("System");// 创建者
            addSocialUser.setCreateTime(nowDate);// 创建时间
            // 获取微信用户信息
            WeChatUserInfo weChatUserInfo = getWeChatUserInfo(weChatLoginBody.getAccessToken(), weChatLoginBody.getOpenId());// 获取微信用户信息
            if (weChatUserInfo != null) {// 用户存在
                addSocialUser.setUnionId(weChatUserInfo.getUnionId()).setUsername(weChatUserInfo.getNickname())
                        .setNickname(weChatUserInfo.getNickname()).setAvatar(weChatUserInfo.getHeadImgUrl()).setGender(weChatUserInfo.getSex());// 用户昵称、头像、性别
            }
            socialUserService.insertSocialUser(addSocialUser);// 添加第三方登录用户信息
        } else {
            SocialUser updateSocialUser = new SocialUser();// 更新第三方登录用户信息
            BeanUtils.copyProperties(socialUser, updateSocialUser);// 复制属性
            updateSocialUser.setUpdateBy("System");// 更新者
            updateSocialUser.setSysUserId(bindSysUserId).setStatus(bindSysUserId == null ? "0" : "1").setAccessToken(weChatLoginBody.getAccessToken())
                    .setRefreshToken(weChatLoginBody.getRefreshToken()).setUuid(uuid);// 绑定系统账号ID、状态、授权码、刷新授权码、第三方登录唯一标识
            socialUserService.updateSocialUser(updateSocialUser);// 更新第三方登录用户信息
        }
        // 没有绑定账号，需要去绑定系统账号,跳转到绑定页面
        if (bindSysUserId == null) {// 没有绑定账号
            AuthUser authUser = new AuthUser();// 用户信息
            authUser.setUuid(uuid);// 第三方登录唯一标识
            authUser.setSource(socialPlatform.getPlatform());// 第三方登录平台
            String bindId = socialLoginService.genBindId(authUser);// 生成绑定ID
            weChatLoginResult.setBindId(bindId);// 返回绑定ID
        } else {
            SysUser sysUser = sysUserService.selectUserById(bindSysUserId);// 根据系统账号ID查询系统账号信息
            if (sysUser == null) {// 系统账号不存在
                throw new ServiceException("用户不存在");
            }
            String token = sysLoginService.redirectLogin(sysUser.getUserName(), sysUser.getPassword());// 生成token
            weChatLoginResult.setToken(token);// 返回token
        }
        return weChatLoginResult;
    }

    /**
     * 小程序微信登录
     * @param weChatLoginBody 微信登录参数
     * @return 登录结果
     */
    @Override
    public WeChatLoginResult miniLogin(WeChatLoginBody weChatLoginBody) {
        // 使用微信手机号去登录不绑定微信，没有用户则用手机号自动注册一个登录，密码是手机号
        SocialPlatformType socialPlatformType = SocialPlatformType.WECHAT_OPEN_MINI_PROGRAM;
        // 查询微信平台信息
        SocialPlatform socialPlatform = socialPlatformService.selectSocialPlatformByPlatform(socialPlatformType.getSourceClient());
        if (socialPlatform == null) {
            throw new ServiceException("请先配置微信公众平台小程序信息！");
        }
        if (StringUtils.isEmpty(weChatLoginBody.getPhoneCode())) {
            throw new ServiceException("用户手机号凭证获取失败，请重新登录！");
        }
        // 先获取token
        WeChatAppResult result = getAccessToken(socialPlatform);
        if (result == null || StringUtils.isEmpty(result.getAccessToken())) {
            throw new ServiceException("获取用户调用凭据失败，请重新登录！");
        }
        // 根据phoneCode获取用户手机号
        WeChatPhoneInfo userPhoneInfo = getWechatUserPhoneInfo(weChatLoginBody.getPhoneCode(), result.getAccessToken());
        if (userPhoneInfo == null || !userPhoneInfo.getErrCode().equals("0")) {
            throw new ServiceException("获取用户手机号失败，请重新登录！");
        }
        String phoneNumber = userPhoneInfo.getPhoneInfo().getPhoneNumber();// 用户手机号
        SysUser sysUser = sysUserService.selectUserByPhoneNumber(phoneNumber);// 查询用户信息
        String token;
        WeChatLoginResult weChatLoginResult = new WeChatLoginResult();// 登录结果
        if (sysUser == null) {
            // 直接用手机号注册一个新用户，密码就是手机号
            RegisterUserInput registerUserInput = new RegisterUserInput();// 注册用户输入参数
            registerUserInput.setUsername(phoneNumber);// 用户名
            registerUserInput.setPhonenumber(phoneNumber);// 手机号
            registerUserInput.setPassword(phoneNumber);// 密码
            RegisterUserOutput registerUserOutput = toolService.registerNoCaptcha(registerUserInput);// 注册用户
            if (StringUtils.isNotEmpty(registerUserOutput.getMsg())) {// 注册失败
                throw new ServiceException(registerUserOutput.getMsg());// 抛出异常
            }
            token = sysLoginService.redirectLogin(phoneNumber, phoneNumber);// 登录
        } else {
            token = sysLoginService.redirectLogin(sysUser.getUserName(), sysUser.getPassword());// 登录
        }
        weChatLoginResult.setToken(token);// 登录token
        return weChatLoginResult;// 返回登录结果
    }

    // 创建或更新社交用户
    private void miniCreateOrUpdateSocialUser(SocialUser socialUser, SocialPlatform socialPlatform, WeChatLoginBody weChatLoginBody, WeChatMiniProgramResult weChatResult, String accessToken, Long bindSysUserId) {
        Date nowDate = DateUtils.getNowDate();// 当前时间
        String uuid = IdUtils.randomUUID();// 随机UUID
        if (socialUser == null) {
            SocialUser insertSocialUser = new SocialUser();// 创建社交用户
            String sourceClient = socialPlatform.getPlatform();// 客户端
            insertSocialUser.setSysUserId(bindSysUserId).setStatus("1").setUuid(uuid).setSource(sourceClient.toUpperCase(Locale.ROOT)).setSourceClient(sourceClient)
                    .setAccessToken(accessToken).setOpenId(weChatResult.getOpenId()).setUnionId(weChatResult.getUnionId())
                    .setCode(weChatLoginBody.getCode()).setDelFlag("0");// 创建社交用户
            insertSocialUser.setCreateBy("System");// 创建人
            insertSocialUser.setCreateTime(nowDate);// 创建时间
            socialUserService.insertSocialUser(insertSocialUser);// 创建社交用户
        } else {
            SocialUser updateSocialUser = new SocialUser();// 更新社交用户
            updateSocialUser.setSocialUserId(socialUser.getSocialUserId());// 社交用户ID
            updateSocialUser.setSysUserId(bindSysUserId).setStatus("1").setCode(weChatLoginBody.getCode());// 更新社交用户
            updateSocialUser.setUpdateBy("System");// 更新人
            socialUserService.updateSocialUser(updateSocialUser);// 更新社交用户
        }
    }

    /**
     * 取消所有相关微信绑定
     * @param wxCancelBindReqVO 微信解绑传参类型
     * @return 结果
     */
    @Override
    public AjaxResult cancelBind(WxCancelBindReqVO wxCancelBindReqVO) {// 取消所有相关微信绑定
        LoginUser loginUser = getLoginUser();// 获取当前登录用户
        if (loginUser == null || loginUser.getUserId() == null) {// 判断当前登录用户是否为空
            throw new ServiceException("请先登录后重试");// 抛出异常
        }
        // 密码验证
        if (VerifyTypeEnum.PASSWORD.getVerifyType().equals(wxCancelBindReqVO.getVerifyType())) {// 判断验证类型是否为密码验证
            if (StringUtils.isEmpty(wxCancelBindReqVO.getPassword())) {// 判断用户密码是否为空
                throw new ServiceException("请传入用户密码");
            }
            Boolean validateResult = sysUserService.validatePassword(loginUser.getUser().getPassword(), wxCancelBindReqVO.getPassword());// 验证用户密码是否正确
            if (Boolean.FALSE.equals(validateResult)) {// 判断验证结果是否为false
                throw new ServiceException("密码错误，请重新输入");
            }
        }
        // 解绑所有微信应用
        int cancelBind = socialUserService.cancelBind(loginUser.getUserId(), SocialPlatformType.listWechatPlatform);// 解绑所有微信应用
        return cancelBind >= 1 ? success("解绑成功") : AjaxResult.error("解绑失败");// 判断解绑结果是否大于等于1
    }

    /**
     * 小程序、移动应用微信绑定
     * @param wxBindReqVO 微信绑定传参类型
     * @return 结果
     */
    @Override
    public AjaxResult bind(WxBindReqVO wxBindReqVO) {
        Long sysUserId = getUserId();
        if (sysUserId == null) {
            throw new ServiceException("请登录后重试");
        }
        String openId = "";
        String unionId = "";
        // 区分小程序绑定还是移动应用绑定
        if (SocialPlatformType.WECHAT_OPEN_MOBILE.sourceClient.equals(wxBindReqVO.getSourceClient())) {
            if (StringUtils.isEmpty(wxBindReqVO.getOpenId()) || StringUtils.isEmpty(wxBindReqVO.getUnionId())) {
                throw new ServiceException("请传入微信用户信息");
            }
            openId = wxBindReqVO.getOpenId();
            unionId = wxBindReqVO.getUnionId();
        } else if (SocialPlatformType.WECHAT_OPEN_MINI_PROGRAM.sourceClient.equals(wxBindReqVO.getSourceClient())) {
            if (StringUtils.isEmpty(wxBindReqVO.getCode())) {
                throw new ServiceException("请传入用户凭证");
            }
            // 查询微信平台信息
            SocialPlatform socialPlatform = socialPlatformService.selectSocialPlatformByPlatform(SocialPlatformType.WECHAT_OPEN_MINI_PROGRAM.sourceClient);
            if (socialPlatform == null) {
                throw new ServiceException("请先配置微信开放平台小程序信息！");
            }
            WeChatMiniProgramResult weChatMiniProgramResult = this.codeToSession(wxBindReqVO.getCode(), socialPlatform);
            if (weChatMiniProgramResult == null
                    || (StringUtils.isEmpty(weChatMiniProgramResult.getOpenId()) && StringUtils.isEmpty(weChatMiniProgramResult.getUnionId()))) {
                throw new ServiceException("获取微信信息失败，请重试！");
            }
            openId = weChatMiniProgramResult.getOpenId();
            unionId = weChatMiniProgramResult.getUnionId();
        }

        SocialUser socialUser = socialUserService.selectOneByOpenIdAndUnionId(openId, unionId);
        int bindResult;
        List<SocialUser> socialUserList = socialUserService.selectBySysUserId(sysUserId);
        if (CollectionUtils.isNotEmpty(socialUserList)) {
            return success("绑定成功！");
        }
        if (socialUser != null) {
            if (socialUser.getSysUserId() != null && !sysUserId.equals(socialUser.getSysUserId())) {
                throw new ServiceException("该微信已绑定其他账号，请先使用微信登录解绑后重试！");
            }
            SocialUser updateSocialUser = new SocialUser();
            updateSocialUser.setSocialUserId(socialUser.getSocialUserId());
            updateSocialUser.setSysUserId(sysUserId).setStatus("1");
            updateSocialUser.setUpdateBy("System");
            bindResult = socialUserService.updateSocialUser(updateSocialUser);
        } else {
            Date nowDate = DateUtils.getNowDate();
            String uuid = IdUtils.randomUUID();
            SocialUser insertSocialUser = new SocialUser();
            insertSocialUser.setSysUserId(sysUserId).setStatus("1").setUuid(uuid).setSource(wxBindReqVO.getSourceClient().toUpperCase(Locale.ROOT)).setSourceClient(wxBindReqVO.getSourceClient())
                    .setOpenId(openId).setUnionId(unionId)
                    .setDelFlag("0");
            insertSocialUser.setCreateBy("System");
            insertSocialUser.setCreateTime(nowDate);
            bindResult = socialUserService.insertSocialUser(insertSocialUser);
        }
        // 绑定
        return bindResult >= 1 ? success("绑定成功！") : AjaxResult.error("绑定失败");
    }

    /**
     * 解绑微信
     */
    @Override
    public AjaxResult getWxBindQr(HttpServletRequest httpServletRequest) {
        Long sysUserId = getUserId();// 用户id
        if (sysUserId == null) {
            throw new ServiceException("请先登录后重试！");
        }
        WeChatLoginQrRes weChatLoginQrRes = new WeChatLoginQrRes();// 返回结果
        SocialPlatform socialPlatform = socialPlatformService.selectSocialPlatformByPlatform(SocialPlatformType.WECHAT_OPEN_WEB_BIND.sourceClient);
        // 获取微信开放平台网站应用个人中心绑定信息
        if (socialPlatform == null) {
            throw new ServiceException("请先配置微信开放平台网站应用个人中心绑定信息");
        }
        weChatLoginQrRes.setAppid(socialPlatform.getClientId());// 应用唯一标识
        weChatLoginQrRes.setScope("snsapi_login");// 静默授权
        weChatLoginQrRes.setState(String.valueOf(System.currentTimeMillis()));// 防止CSRF攻击
        String wxBindId = socialLoginService.genWxBindId(sysUserId);// 生成绑定ID
        String url = socialPlatform.getRedirectUri() + wxBindId;// 回调地址
        weChatLoginQrRes.setRedirectUri(url);// 回调地址
        return success(weChatLoginQrRes);// 返回二维码
    }

    @Override
    public String wxBindCallback(String code, String state, String wxBindId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        SocialPlatform socialPlatform = socialPlatformService.selectSocialPlatformByPlatform(SocialPlatformType.WECHAT_OPEN_WEB_BIND.sourceClient);
        if (socialPlatform == null) {
            String serverName = httpServletRequest.getServerName();
            String msgId = socialLoginService.genErrorId("请先配置微信开放平台网站应用个人中心绑定信息");
            return "https://" + serverName + "/user/profile?wxBindMsgId=" + msgId;
        }
        String url = socialPlatform.getRedirectLoginUri();
        //获取临时票据 code
        log.info("code:{}", code);
        if (StringUtils.isEmpty(code)) {
            String msgId = socialLoginService.genErrorId("您已取消授权或未获取到授权信息");
            return url + msgId;
        }
        Long sysUserId = redisCache.getCacheObject(WX_BIND_REDIS_KEY + wxBindId);
        if (sysUserId == null) {
            String msgId = socialLoginService.genErrorId("二维码已失效，请重新点击绑定");
            return url + msgId;
        }
        List<SocialUser> socialUserList = socialUserService.selectBySysUserId(sysUserId);
        if (CollectionUtils.isNotEmpty(socialUserList)) {
            String msgId = socialLoginService.genErrorId("您的账号已绑定微信，请先解绑");
            return url + msgId;
        }
        // 组装获取accessToken的url
        WeChatAppResult weChatAppResult = this.getAccessTokenOpenId(code, socialPlatform);
        if (weChatAppResult == null || StringUtils.isEmpty(weChatAppResult.getAccessToken())
                || StringUtils.isEmpty(weChatAppResult.getOpenId()) || StringUtils.isEmpty(weChatAppResult.getUnionId())) {
            String msgId = socialLoginService.genErrorId("获取微信信息失败，请重试");
            return url + msgId;
        }
        Long bindUserId = socialUserService.selectSysUserIdByUnionId(weChatAppResult.getUnionId());
        if (bindUserId != null && !bindUserId.equals(sysUserId)) {
            String msgId = socialLoginService.genErrorId("您的微信已绑定其他账号，请先使用微信登录解绑后重试！");
            return url + msgId;
        }
        SocialUser socialUser = socialUserService.selectOneByOpenIdAndUnionId(weChatAppResult.getOpenId(), weChatAppResult.getUnionId());
        if (socialUser == null) {
            socialUser = new SocialUser();
        }
        WeChatUserInfo weChatUserInfo = this.getWeChatUserInfo(weChatAppResult.getAccessToken(), weChatAppResult.getOpenId());
        String uuid = IdUtils.randomUUID();
        Date nowDate = DateUtils.getNowDate();
        socialUser.setUuid(uuid).setAccessToken(weChatAppResult.getAccessToken()).setRefreshToken(weChatAppResult.getRefreshToken())
                .setOpenId(weChatUserInfo.getOpenId()).setUnionId(weChatUserInfo.getUnionId()).setSysUserId(sysUserId).setStatus("1")
                .setNickname(weChatUserInfo.getNickname()).setUsername(weChatUserInfo.getNickname()).setAvatar(weChatUserInfo.getHeadImgUrl());
        // 个人中心绑定这个也做了一个配置，标识设为了 wechat_open_web_bind,但其实都属于网站应用，所以这里还是改为使用wechat_open_web这个来记录来源吧,统一为网站来源，方便查询
        socialUser.setSource(SocialPlatformType.WECHAT_OPEN_WEB.sourceClient).setSourceClient(SocialPlatformType.WECHAT_OPEN_WEB.sourceClient);
        if (socialUser.getCreateTime() == null) {
            socialUser.setCreateTime(nowDate);
            socialUser.setCreateBy("System");
        }
        socialUser.setUpdateTime(nowDate);
        socialUser.setUpdateBy("System");
        int updateResult = socialUser.getSocialUserId() == null ? socialUserService.insertSocialUser(socialUser) : socialUserService.updateSocialUser(socialUser);
        String msg = updateResult >= 1 ? "绑定成功" : "绑定失败";
        String msgId = socialLoginService.genErrorId(msg);
        return url + msgId;
    }

    @Override
    public AjaxResult getWxBindMsg(String wxBindMsgId) {
        String errorMsg = redisCache.getCacheObject(LOGIN_ERROR_MSG_REDIS_KEY + wxBindMsgId);
        if (StringUtils.isEmpty(errorMsg)) {
            return success();
        } else if ("绑定成功".equals(errorMsg)) {
            return success(errorMsg);
        } else {
            return error(errorMsg);
        }
    }

    /**
     * 移动应用获取微信用户accessToken
     * @param code 用户登录code
     * @return WeChatAppResult
     */
    private WeChatAppResult getAccessTokenOpenId(String code, SocialPlatform socialPlatform) {
        String url = WE_CHAT_APP_API_CODE_TO_ACCESS_TOKEN_URL + "?appid=" + socialPlatform.getClientId() + "&secret=" + socialPlatform.getSecretKey() + "&code=" + code + "&grant_type=authorization_code";
        String s = HttpUtils.sendGet(url);
        return JSON.parseObject(s, WeChatAppResult.class);
    }

    /**
     * 移动应用获取微信用户信息
     * @param accessToken 接口调用凭证
     * @param openId 用户唯一标识
     * @return WeChatUserInfo
     */
    private WeChatUserInfo getWeChatUserInfo(String accessToken, String openId) {
        String url = WE_CHAT_API_TO_USER_INFO_URL + "?access_token=" + accessToken + "&openid=" + openId;
        String s = HttpUtils.sendGet(url);
        return JSON.parseObject(s, WeChatUserInfo.class);
    }

    /**
     * 小程序获取微信用户登录信息
     * @param code 用户凭证
     * @param socialPlatform 登录平台
     * @return 结果
     */
    private WeChatMiniProgramResult codeToSession(String code, SocialPlatform socialPlatform) {
        String url = WE_CHAT_MINI_PROGRAM_API_CODE_TO_ACCESS_TOKEN_URL + "?appid=" + socialPlatform.getClientId() + "&secret=" + socialPlatform.getSecretKey() + "&js_code=" + code + "&grant_type=authorization_code";
        String s = HttpUtils.sendGet(url);
        return JSON.parseObject(s, WeChatMiniProgramResult.class);
    }

    /**
     * 小程序获取微信用户手机号
     * @param code 凭证
     * @return 手机号信息
     */
    private WeChatPhoneInfo getWechatUserPhoneInfo(String code, String accessToken) {
        String url = WE_CHAT_API_TO_USER_PHONE_INFO_URL + accessToken;
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        String s = HttpUtils.sendPost(url, JSONObject.toJSONString(map));
        return JSON.parseObject(s, WeChatPhoneInfo.class);
    }

    /**
     * 小程序获取微信用户accessToken
     * @param socialPlatform 用户登录code
     * @return WeChatAppResult
     */
    private WeChatAppResult getAccessToken(SocialPlatform socialPlatform) {
        String url = WE_CHAT_MINI_PROGRAM_API_TO_ACCESS_TOKEN_URL + "&appid=" + socialPlatform.getClientId() + "&secret=" + socialPlatform.getSecretKey();
        String s = HttpUtils.sendGet(url);
        return JSON.parseObject(s, WeChatAppResult.class);
    }
}
