package com.bsit.obu_issue;

/**
 * Created by bsit on 2016/3/23.
 */
public class CommonConstant {
    public static final String BACKUP_DB = "/coband/backup/";
    public static final String HEXFILE = "COBANDW3V1.00.BIN";
    public static final String SCAN_WATCH_DEVICE_NAME = "coband W3";
        public static final String POSID = "470500730001";//手环正式
//    public static final String POSID = "410399990001";//手环测试
    public static final String LOSS_ORDER = "0";
    public static final String ALL_ORDER_LIST = "0";//全部订单列表
    public static final String QINDAOTONG_ORDER_LIST = "1";//手环订单列表
    public static final String COBAND_ORDER_LIST = "2";//公交卡订单列表
    public static final String CODE_ORDER_LIST = "3";//二维码订单列表
    public static final String RUQUEST_SUCSSECE = "0";//阿里云接口返回成功标志
    public static final String RUQUESTSUCSSECE = "1";//青岛通接口返回成功标志
    public static final String TRADETYPE = "APP";
    public static final String UPDATE = "1";//强制升级
    public static final String APP_ID = "wx5d0dae598429e979";//微信支付appid
    public static final String PARTNER_ID = "1329539301";//微信支付商户id
    public static final String JD_APPID = "jdjr111081761001";//JD APPid
    public static final String JD_MERCHANTID = "111081761003";//JD支付商户id
    //掌上充
    public static final String ZSC_POSID = "470500730002";//A51正式
    public static final String CDB_POSID = "470500730008";//充电宝正式
//    public static final String ZSC_POSID = "410399990001";//A51测试
//    public static final String CDB_POSID = "410399990001";//充电宝测试
    public static final String ZSC_UPDATA_RUQUEST_SUCSSECE = "1";
    public static final int ZSC_SEND_PACKAGE_MAX_NUM = 900;//每次发送固件数据包数为 300 * 8
    public static String ZSC_READ_CARD_TAG = "CARD";
    public static String ZSC_BLE_TAG = "BLE";
    public static String ZSC_CONTROL_TAG = "APP";
    public static String ZSC_DEVICE_ID = "";

    public static final String ALIPAY = "1";
    public static final String WX_PAY = "2";
    public static final String OFFLINE_PAY = "3";
    public static final String JD_PAY = "4";

    /**
     * 获取服务器固件版本，请求参数
     */
    public static String ZSCP207_VERSION_REQUEST_PARAMS = "P207-A51";
    public static String ZSCP209_VERSION_REQUEST_PARAMS = "P209";
    /**
     * 写入文件超时时长
     */
    public static long ZSC_DOWNLOAD_TIMEOUT = 5000;
    public static int ZSC_RING_CARD_LENGTH = 20;
    public static int ZSC_CARD_LENGTH = 16;
    public static int ZSC_MAC_LENGTH = 12;//mac地址长度

    public static String WORK_KEY = "CE6319B34008CAECE64008CA4ECE631A";//工作密钥
    public static String QRCODE_KEY = "795B09643378CD46F33AFB8731138B8A";//二维码密钥
//        public static String ETC_CARD = "0069";//etc卡类型（测试用）
    public static String ETC_CARD = "0070";//etc卡类型（正式用）

    public static String DEVICE_ID = "";
    public static final String OPTION_MODE_SHARE_PREFERENCE_NAME = "optionMode";
}
