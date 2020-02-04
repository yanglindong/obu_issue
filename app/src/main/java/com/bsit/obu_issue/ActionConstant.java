package com.bsit.obu_issue;

/**
 * Created by bsit on 2016/3/23.
 */
public class ActionConstant {

    /**
     * 蓝牙连接，设备信息
     */
    public static final String BLE_CONNECTSTATE_CHANGE = "action.ble.connect.change";// 蓝牙连接成功
//    public static final String BLE_CONNECT_INIT= "action.ble.connect.init";// 蓝牙连接后初始化动作完成
    public static final String BLE_BINDSTATE_CHANGE = "action.ble.bindstate.change";// 蓝牙绑定状态发生变化
    public static final String BLE_UNBINDSTATE = "action.ble.ubind";// 蓝牙解除绑定
    public static final String BLE_SCANBLE_DEVICE = "action.ble.scan.ble";// 搜素蓝牙设备
    public static final String BLE_GETDEVICEINFO_CHANGE = "action.ble.getdeviceinfo";// 获取设备信息

    /**
     * 设置提醒
     */
    public static final String BLE_NOTIFY_SETTING = "action.notify.setting"; //提醒设置
    public static final String BLE_ANLILOSS_SETTING = "action.coband.send.anlilossSet";// 防丢设置变更
    public static final String BLE_LONG_TIME_SIT_SETTING = "action.coband.send.longtimeSitSet";// 久坐设备提醒
    public static final String BLE_ALARM_SET = "com.coband.ble.set.alarm";// 设置闹钟
    public static final String BLE_TIME_SET = "com.coband.ble.set.time";// 设置时间
    public static final String BLE_LIFT_WRIST_SET = "com.coband.ble.lift.wrist";// 设置抬腕
    public static final String BLE_LIFT_WRIST_STATUS_R = "com.coband.ble.lift.wrist_status_r";// 获取设置抬腕状态回复

    public static final String BLE_MODLE_SET = "com.coband.ble.nodle.set";// 初始化震动模式成功
    public static final String BLE_LONG_TIME_SET = "action.coband.set.ble.longtimeSitSet";// 初始化久坐设置成功
    public static final String BLE_LONG_TIME_GET = "action.coband.get.ble.longtimeSitSet";// 获取久坐设置成功
    public static final String BLE_ANLILOSS_SET = "action.coband.set.anlilossSet";// 初始化防丢设置成功
    public static final String BLE_GOAL_SETTING = "com.coband.goal.seting";// 运动目标查询

    public static final String BLE_GET_ALARM_SETING = "com.coband.ble.get.seting.alarm";// 获取闹钟设置
    /**
     * 拍照
     */
    public static final String SPORT_TAKE_PHOTO = "com.sport.take.photo";// 拍照
    public static final String SPORT_TAKE_PHOTO_START = "com.sport.take.photo.start";// 准备开始拍照
    public static final String SPORT_TAKE_PHOTO_END = "com.sport.take.photo.end";// 结束拍照

    /**
     * 数据更新
     */
    public static final String REFRESH_BASEDATA = "action.coband.refresh.basedata";// 用于更新本地个人设置数据
    public static final String REFRESH_SPORTDATA = "action.coband.refresh.sportdata";// 用于更新运动数据
    public static final String REFRESH_SLEEPDATA = "action.coband.refresh.sleepdata";// 用于更新睡眠数据
    public static final String REFRESH_DEEPLIGHT = "action.coband.refresh.deepandlight";// 用于更新深睡浅睡睡眠数据

    /**
     * 固件升级
     */
    public static final String OTA_UPDATE_RELUST = "action.coband.ota.update.result";// 请求固件升级返回结果
    public static final String OTA_UPDATE = "action.coband.ota.update";// 固件升级完成
    public static final String OTA_UPDATE_FINISH = "action.coband.ota.update.finish";// 固件升级结束
    public static final String BROADCAST_PROGRESS = "no.nordicsemi.android.dfu.broadcast.BROADCAST_PROGRESS"; //进度变化
    public static final String OTA_START = "action.coband.ota.update.start"; //固件升级开始
    public static final String OTA_CRC_FAILD = "action.coband.ota.update.error"; //crc校验失败
    public static final String OTA_FAILD = "action.coband.ota.update.faild"; //crc校验失败

    /**
     * 充值余额
     */
    public static final String TOP_UP = "action.coband.topup.info";// 充值返回信息
    public static final String TOP_UP_MAC2 = "action.coband.topup.mac2.info";// 充值返回信息
    public static final String QUREY_SUM = "action.coband.query.info";//余额查询返回信息
    public static final String QUREY_ORDER = "action.coband.query.order.info";//余额查询返回信息

    /**
     * 提醒
     */
    public static final String UPDATE_APP = "com.bsit.updatapp";    // 版本更新
    public static final String SYS_NOTIFICATION = "com.coband.activity.NotificationFetcherService";// 通知事件

    public static final String ZSC_TOP_UP = "action.zsc.init.topup.info";// 圈存初始化返回信息
    public static final String ZSC_TOP_UP_MAC2 = "action.zsc.topup.info";// 圈存返回信息
    public static final String FIND_CARD_FAILD_TAG = "action.zsc.find.card.failed";//寻卡失败
    public static final String CARD_INFO_GET_SUCCESS_TAG = "action.zsc.card.info.success";//获取卡信息成功
    public static final String CARD_INFO_GET_TAG = "action.zsc.card.info";//获取卡信息
    public static final String BLE_CONNECT_FAILED = "action.zsc.ble.connect.failed";//蓝牙连接失败
    public static final String BLE_CONNECT_SUCCESS = "action.zsc.ble.connect.success";//蓝牙连接成功
    public static final String BLE_STATUS = "action.zsc.ble.status";//蓝牙连接状态
    public static final String ZHC_UPDATE_APP = "com.bsit.updatapp";// 版本更新
    public static final String GET_VERSION_INFO_SUCCESS = "action.zsc.get.current.version.info.success";//获取固件版本信息
    public static final String BLE_GET_DATA_ERROR = "action.zsc.ble.get.data.error";//发送升级包数据回复错误
    public static final String QUERY_REMIND_FINISH = "action.coband.query_remind_finish";//获取提醒
    public static final String DEFAULT_CMD = "action.coband.default_cmd";
    public static final String ANTI_LOST_SET_SUCC = "action.coband.anti_lost_set_succ";
    public static final String ALARM_SET_SUCC = "action.coband.alarm_succ";
    public static final String FIND_WATCH_SUCC = "action.coband.find_watch_succ";
    public static String START_SEND_PACKAGE = "action.zsc.start.send.update.package";//发送升级包开始

    //ETC充值
    public static String ETC_REQUEST_FIALD = "action.etc.net.request.fiald";//ETC充值请求失败
    public static String ETC_REQUEST_QUERY_SUCESECC = "action.etc.net.request.query_sucess";//ETC查询余额
    public static String ETC_REQUEST_TOPUP_SUCESECC = "action.etc.net.request.toup_sucess";//ETC查询充值

    public static String REFRESH_SPORT_STEPS = "action.mobile.sport.step_change";
    public static final String QUERY_REMIND = "action.coband.query_remind";
}
