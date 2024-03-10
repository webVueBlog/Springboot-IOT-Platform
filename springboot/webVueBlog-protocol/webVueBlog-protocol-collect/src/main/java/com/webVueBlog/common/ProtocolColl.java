package com.webVueBlog.common;

import com.webVueBlog.protocol.base.protocol.IProtocol;
import lombok.Data;

@Data
public class ProtocolColl {// 协议集合

    private IProtocol protocol;// 协议

    private Long productId;// 产品id
}
