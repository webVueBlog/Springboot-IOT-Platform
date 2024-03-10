package com.webVueBlog.protocol.enums;

import com.webVueBlog.common.core.protocol.modbus.ModbusCode;

public interface IModbusRtu {

    /** 自定义上报报文头*/
    byte[] BEGIN_BYTES =   {(byte) 0xFA};
    /**自定义报文*/
    byte[] END_BYTES =     {(byte) 0x0D};
    /**指令下发服务报文头*/
    byte[] BEGIN_BYTES_UP = {(byte) 0xFD};
    /**默认下发从机设备编号*/
    byte[] DEV_CODE =     {ModbusCode.Read01.getCode()};
    /**下发编号*/
    byte[] SEND_SINGLE =  {ModbusCode.Write06.getCode()};

    byte[] READ_SINGLE =  {ModbusCode.Read03.getCode()};

    byte[] READ_BATCH =   {ModbusCode.Write10.getCode()};

    /*默认下发的寄存器地址个数*/
    byte[] ADDRESS_NUM =  {(byte) 0x00,(byte) 0x01};

    /**
     * 设备上行数据的数据下标
     * <br>
     * <table border='1'>
     * <tr> <th>BEGIN_INDEX</th> <th>ADDRESS_INDEX</th> <th>DEVICE_INDEX</th> ></tr>
     * <tr><th>包头</th>      <th>寄存器地址位</th>      <th>设备编码位</th> </tr>
     * <tr> <th>COMMAND_INDEX</th> <th>DATA_LENGTH_INDEX</th> <th>DATA_INDEX</th></tr>
     * <tr><th>指令标识位/查询/修改</th> <th>数据长度位</th>    <th>数据起始位</th> </tr>
     * </table>
     * <br>
     */
    int BEGIN_INDEX = 0, ADDRESS_INDEX = 1, DEVICE_INDEX = 3,
            COMMAND_INDEX = 4, DATA_LENGTH_INDEX = 5, DATA_INDEX = 6;

    /**
     * 设备上行数据的数据下标 标准RTU
     * <br>
     * <table border='1'>
     * <tr>  <th>DEVICE_INDEX</th>  <th>COMMAND_INDEX</th> <th>DATA_LENGTH_INDEX</th> </tr>
     * <tr>   <th>从机编号</th>     <th>指令标识位/查询/修改</th> <th>数据长度位</th>    </tr>
     * </table>
     * <br>
     */
    int SALVE_ID = 0,  COMMAND_RTU = 1, DATA_LENGTH_RTU= 2, DATA_INDEX_RTU=3;

    /**
     * 设备下行数据的数据下标
     * <br>
     * <table border='1'>
     * <tr> <th>DEVICE_INDEX_UP</th> <th>COMMAND_INDEX_UP</th> <th>ADDRESS_INDEX_UP</th> <th>DATA_INDEX_UP</th> ></tr>
     * <tr><th>设备编码位起始位</th> <th>指令标识位/修改起始位</th>  <th>寄存器地址起始位</th> <th>数据起始位</th></tr>
     * </table>
     * <br>
     */
    int BEGIN_INDEX_UP = 0, MESSAGE_ID_INDEX_UP = 1, DEVICE_INDEX_UP = 7, COMMAND_INDEX_UP = 8, ADDRESS_INDEX_UP = 9, DATA_INDEX_UP = 11;

    /**
     * 数据长度 <br>
     * <table border='1'>
     * <tr>
     * <tr><th>BEGIN_SIZE</th> <th>MESSAGE_ID_SIZE</th> <th>ADDRESS_SIZE</th> <th>DEVICE_SIZE</th> <th>COMMAND_SIZE</th> </tr>
     * <tr><th>DATA_LENGTH_SIZE</th> <th>CHECK_CODE_SIZE</th> <th>END_SIZE</th></tr>
     * <tr> <th>包头长度</th> <th>消息ID长度</th> <th>寄存器地址长度</th> <th>设备位长度</th>  <th>指令标识位长度</th>   </tr>
     * <tr><th>数据长度位长度</th> <th>校验位长度</th> <th>包尾长度</th> </tr>
     * </table>
     * <br>
     */
    int BEGIN_SIZE = 1, MESSAGE_ID_SIZE = 6, ADDRESS_SIZE = 2, DEVICE_SIZE = 1, COMMAND_SIZE = 1,
            DATA_LENGTH_SIZE = 2, CHECK_CODE_SIZE = 2, END_SIZE = 1;
}
