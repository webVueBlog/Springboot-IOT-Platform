package com.webVueBlog.common.constant;

/**
 * 常量
 */
public interface DaConstant {

    interface SERVER{
        String UFT8 = "UTF-8";
        String GB2312 = "GB2312";


        String MQTT = "mqtt";
        String PORT = "port";
        String ADAPTER = "adapter";
        String FRAMEDECODER ="frameDecoder";// 解码器
        String DISPATCHER = "dispatcher";// 分发器
        String DECODER = "decoder";// 解码器
        String ENCODER = "encoder";// 编码器
        String MAXFRAMELENGTH = "maxFrameLength";// 最大帧长度
        String SLICER = "slicer";// 分片器
        String DELIMITERS = "delimiters";// 分隔符
        String IDLE = "idle";// 空闲时间
        String WS_PREFIX = "web-";
        String WM_PREFIX = "server-";
        String FAST_PHONE = "phone-";

        /*MQTT平台判定离线时间 keepAlive*1.5 */
        Long DEVICE_PING_EXPIRED = 90000L;
    }

    interface CLIENT{//客户端*/
        //加盐
        String TOKEN = "webVueBlog";
    }

    /*webSocket配置*/
    interface WS{
        String HEART_BEAT = "heartbeat";/*心跳时间*/
        String HTTP_SERVER_CODEC = "httpServerCodec";
        String AGGREGATOR = "aggregator";/*聚合器*/
        String COMPRESSOR = "compressor";/*压缩器*/
        String PROTOCOL = "protocol";/*协议*/
        String MQTT_WEBSOCKET = "mqttWebsocket";/*MQTT协议*/
        String DECODER = "decoder";//解码器
        String ENCODER = "encoder";//编码器
        String BROKER_HANDLER = "brokerHandler";//代理处理器

    }

    interface TASK{
        /**设备上下线任务*/
        String DEVICE_STATUS_TASK = "deviceStatusTask";
        /**设备主动上报任务*/
        String DEVICE_UP_MESSAGE_TASK = "deviceUpMessageTask";
        /**设备回调任务*/
        String DEVICE_REPLY_MESSAGE_TASK = "deviceReplyMessageTask";
        /**设备下行任务*/
        String DEVICE_DOWN_MESSAGE_TASK = "deviceDownMessageTask";
        /**服务调用(指令下发)任务*/
        String FUNCTION_INVOKE_TASK = "functionInvokeTask";
        /**属性读取任务,区分服务调用*/
        String DEVICE_FETCH_PROP_TASK = "deviceFetchPropTask";
        /**
         * 设备其他消息处理
         */
        String DEVICE_OTHER_TASK = "deviceOtherMsgTask";
        /**消息消费线程*/
        String MESSAGE_CONSUME_TASK = "messageConsumeTask";
        /*内部消费线程publish*/
        String MESSAGE_CONSUME_TASK_PUB = "messageConsumeTaskPub";
        /*内部消费线程Fetch*/
        String MESSAGE_CONSUME_TASK_FETCH = "messageConsumeTaskFetch";
        /*OTA升级延迟队列*/
        String DELAY_UPGRADE_TASK = "delayUpgradeTask";

    }

    interface MQTT{
       //*上报平台前缀*//*
        String UP_TOPIC_SUFFIX = "post";
        //*下发设备前缀*//*
        String DOWN_TOPIC_SUFFIX = "get";

        /*模拟设备后缀*/
        String PROPERTY_GET_SIMULATE = "simulate";//属性读取

        String PREDIX = "/+/+";//设备标识符

        String DUP = "dup";//消息是否是重复的
        String QOS = "qos";//消息的服务质量
        String RETAIN = "retain";//消息是否被服务器保留
        String CLEAN_SESSION = "cleanSession";//客户端和服务器是否持久保持连接

        /*集群方式*/
        String REDIS_CHANNEL = "redis";
        String ROCKET_MQ = "rocketmq";
    }

    /*集群，全局发布的消息类型*/
    interface CHANNEL {
        /*设备状态*/
        String DEVICE_STATUS   = "device_status";
        /*平台读取属性*/
        String PROP_READ       = "prop_read";
        /*推送消息*/
        String PUBLISH         = "publish";
        /*服务下发*/
        String FUNCTION_INVOKE = "function_invoke";
        /*事件*/
        String EVENT = "event";
        /*other*/
        String OTHER = "other";
        /*Qos1 推送应答*/
        String PUBLISH_ACK     = "publish_ack";
        /*Qos2 发布消息收到*/
        String PUB_REC         = "pub_rec";
        /*Qos 发布消息释放*/
        String PUB_REL = "pub_rel";
        /*Qos2 发布消息完成*/
        String PUB_COMP = "pub_comp";

        String UPGRADE  = "upgrade";//设备升级

        /*-------------------------ROCKETMQ-------------------------*/
        String SUFFIX = "group";
        /*设备状态*/
        String DEVICE_STATUS_GROUP   = DEVICE_STATUS +SUFFIX;
        /*平台读取属性*/
        String PROP_READ_GROUP        = PROP_READ + SUFFIX;
        /*服务下发*/
        String FUNCTION_INVOKE_GROUP = FUNCTION_INVOKE + SUFFIX;
        /*推送消息*/
        String PUBLISH_GROUP         = PUBLISH + SUFFIX;
        /*Qos1 推送应答*/
        String PUBLISH_ACK_GROUP     = PUBLISH_ACK +SUFFIX;
        /*Qos2 发布消息收到*/
        String PUB_REC_GROUP         = PUB_REC + SUFFIX;
        /*Qos 发布消息释放*/
        String PUB_REL_GROUP         = PUB_REL + SUFFIX;
        /*Qos2 发布消息完成*/
        String PUB_COMP_GROUP        = PUB_COMP + SUFFIX;
        /*OTA升级*/
        String UPGRADE_GROUP         = UPGRADE + SUFFIX;
    }



    /**redisKey 定义*/
    interface REDIS{
        /*redis全局前缀*/
        String GLOBAL_PREFIX_KEY = "da:";
        /*设备在线状态*/
        String DEVICE_STATUS_KEY =  "device:status";
        /*在线设备列表*/
        String DEVICE_ONLINE_LIST = "device:online:list";
        /*设备实时状态key*/
        String DEVICE_RUNTIME_DATA = "device:runtime:";
        /*通讯协议参数*/
        String DEVICE_PROTOCOL_PARAM = "device:param:";
        /**设备消息id缓存key*/
        String DEVICE_MESSAGE_ID = "device:messageid";
        /**固件版本key*/
        String FIRMWARE_VERSION = "device:firmware:";//固件版本key

        /**采集点变更记录缓存key*/
        String COLLECT_POINT_CHANGE = "collect:point:change:";//采集点变更记录缓存key
        /**属性下发回调*/
        String PROP_READ_STORE = "prop:read:store:";//属性下发回调
        /**sip*/
        String RECORDINFO_KEY = "sip:recordinfo:";//recordinfo是sip协议中的一个命令
        String DEVICEID_KEY = "sip:deviceid:";//deviceid是sip协议中的一个命令
        String STREAM_KEY = "sip:stream:";//stream是sip协议中的一个命令
        String SIP_CSEQ_PREFIX = "sip:CSEQ:";//sip协议中的一个命令
        String DEFAULT_SIP_CONFIG = "sip:config";//sip:config是sip协议中的一个命令
        String DEFAULT_MEDIA_CONFIG = "sip:mediaconfig";//sip:mediaconfig是sip协议中的一个命令


        /**当前连接数*/
        String MESSAGE_CONNECT_COUNT = "messages:connect:count";
        /**总保留消息*/
        String MESSAGE_RETAIN_TOTAL = "message:retain:total";

        /**主题数*/
        String MESSAGE_TOPIC_TOTAL = "message:topic:total";
        /*发送消息数*/
        String MESSAGE_SEND_TOTAL = "message:send:total";
        /*接收消息数*/
        String MESSAGE_RECEIVE_TOTAL = "message:receive:total";
        /*连接次数*/
        String MESSAGE_CONNECT_TOTAL = "message:connect:total";
        /**认证次数*/
        String MESSAGE_AUTH_TOTAL = "message:auth:total";
        /**订阅次数*/
        String MESSAGE_SUBSCRIBE_TOTAL = "message:subscribe:total";

        /**今日接收消息*/
        String MESSAGE_RECEIVE_TODAY = "message:receive:today";
        /**今日发送消息*/
        String MESSAGE_SEND_TODAY = "message:send:today";


        // 物模型值命名空间：Key：TSLV:{productId}_{deviceNumber}   HKey:{identity#V/identity#S/identity#M/identity#N}
        /**
         * v-值
         * s-影子值
         * m-是否为检测值
         * n-名称
         */
         String DEVICE_PRE_KEY = "TSLV:";//设备物模型值命名空间

        // 物模型命名空间：Key:TSL:{productId}
         String TSL_PRE_KEY ="TSL:";//物模型命名空间

         /**modbus缓存指令*/
         String POLL_MODBUS_KEY = "poll:modbus";//modbus缓存指令


    }

    interface TOPIC{
        /*属性上报*/
        String PROP = "properties";
        //事件
        String EVENT = "events";
        //功能
        String FUNCTION = "functions";
        /*非OTA消息回复*/
        String MSG_REPLY = "message/reply";
        /*OTA升级回复*/
        String UPGRADE_REPLY = "upgrade/reply";
        String SUB_UPGRADE_REPLY = "sub/upgrade/reply";//订阅OTA升级回复
        /*网关子设备结尾*/
        String SUB = "/sub";
    }

    interface PROTOCOL {//protocol
        String ModbusRtu = "MODBUS-RTU";//modbus-rtu
        String YinErDa = "YinErDa";//银尔达
        String JsonObject = "JSONOBJECT";//jsonobject
        String JsonArray = "JSON";//jsonarray
        String ModbusRtuPak = "MODBUS-RTU-PAK";//modbus-rtu-pak
        String FlowMeter = "FlowMeter";//流量计
        String RJ45 = "RJ45";//rj45
        String ModbusToJson = "MODBUS-JSON";//modbus-json
        String ModbusToJsonFY = "MODBUS-JSON-FY";//modbus-json-fy
        String JsonObject_ChenYi = "JSONOBJECT-CHENYI";//jsonobject-chenyi


    }

}
