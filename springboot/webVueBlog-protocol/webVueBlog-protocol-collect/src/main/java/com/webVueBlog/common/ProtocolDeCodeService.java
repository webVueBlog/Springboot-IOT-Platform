package com.webVueBlog.common;

import com.webVueBlog.common.core.iot.response.DeCodeBo;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.gateway.CRC16Utils;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 协议编解码
 *
 */
@Component
@Slf4j
public class ProtocolDeCodeService {


    public String protocolDeCode(DeCodeBo bo) {
        if (null == bo) {
            throw new ServiceException("输入内容为空");
        }
        String payload = bo.getPayload();
        /*1-解析 2-读指令 3-写指令 4-CRC生成 5-CRC校验*/
        switch (bo.getType()) {
            case 1:
            case 2:
            case 3:
            case 4:
                byte[] crc16Byte = ByteBufUtil.decodeHexDump(payload);//16进制转byte
                String crc = CRC16Utils.getCRC(crc16Byte);//crc校验
                return payload + crc;//拼接crc
            case 5:
                byte[] crcByte = ByteBufUtil.decodeHexDump(payload);//16进制转byte
                byte[] checksCRC = {crcByte[crcByte.length -2],crcByte[crcByte.length-1]};//获取crc校验值
                byte[] sourceCRC = ArrayUtils.subarray(crcByte, 0, crcByte.length - 2);//获取原报文
                String crc1 = CRC16Utils.getCRC(sourceCRC);//crc校验
                String check = ByteBufUtil.hexDump(checksCRC);//16进制转字符串
                if (!crc1.equalsIgnoreCase(check)){//校验失败
                    return "原报文CRC:" + check +"校验失败,CRC值应为:" + crc1 +
                            "<br/>完整报文:" + ByteBufUtil.hexDump(sourceCRC) +crc1;
                }else {
                    return "校验通过！";
                }
        }
        return null;
    }


}
