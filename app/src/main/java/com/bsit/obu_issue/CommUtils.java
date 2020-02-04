package com.bsit.obu_issue;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;



import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * JET 公共方法
 *
 * @author Administrator
 */
public class CommUtils {
    /**
     * 正则表达式：验证邮箱
     */
    private static final Pattern emailRex = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");

    /**
     * 校验电话号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return mobile != null && mobile.trim().length() == 11;
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return email != null && emailRex.matcher(email).matches();
    }

    /**
     * 计算以2660开头的卡号的卡面号(卡内号转卡面号)
     *
     * @param cardId 以2660开头的卡号不包含2660
     */
    public static String getCardNo(String cardId) {
        int M1 = 31;
        int[] b1 = {5, 6, 8, 1, 0, 2, 3, 7, 9, 4};
        int[] b2 = {4, 8, 1, 3, 7, 2, 5, 9, 0, 6};
        int[] b3 = {1, 7, 2, 6, 8, 5, 9, 3, 4, 0};
        int[] b4 = {7, 9, 0, 3, 1, 2, 6, 8, 4, 5};
        int C1, C2, C3, C4;
        int bt;
        int ct1 = 0;
        int ct2 = 0;
        int ct3 = 0;
        int ct4 = 0;
        for (int i = 0; i < 12; i++) {
            bt = cardId.charAt(i) - '0';
            ct1 += bt;
            ct1 += b1[bt];
            ct2 += bt;
            ct2 += b2[bt];
            ct3 += bt;
            ct3 += b3[bt];
            ct4 += bt;
            ct4 += b4[bt];
        }
        C1 = (ct1 + M1) % 9;
        C2 = (ct2 + M1) % 9;
        C3 = (ct3 + M1) % 9;
        C4 = (ct4 + M1) % 9;
        return cardId + C1 + C2 + C3 + C4;
    }

    public static boolean isNum(String str) {
        Pattern pattern = Pattern
                .compile("^\\d+(\\.\\d+)?$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 格式化字符串 0
     *
     * @return
     */
    public static String formatFloat(double f) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(f);
    }

    /**
     * 格式化字符串 00.0
     *
     * @return
     */
    public static String formatFloat1(String str) {
        if (str == null || str.equals("")) str = "0.00";
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(Double.parseDouble(str));
    }

    /**
     * 返回xxhxxm
     */
    public static String getTimeFormat(int min) {
        int h = min / 60;
        int m = min % 60;
        return h + "小时" + m + "分钟";
    }

    /**
     * 步数
     *
     * @return
     */
    public static List<String> getSteps() {
        List<String> hour = new ArrayList();
        for (int j = 1; j <= 50; j++) {
            hour.add(j * 1000 + "");
        }
        return hour;
    }

    /**
     * 小时数
     *
     * @return
     */
    public static List<String> getHour() {
        List<String> hour = new ArrayList();
        for (int j = 0; j <= 23; j++) {
            hour.add(j < 10 ? "0" + j : "" + j);
        }
        return hour;
    }

    /**
     * 夜间小时数
     *
     * @return
     */
    public static List<String> getNightHour() {
        List<String> hour = new ArrayList();
        for (int j = 20; j <= 23; j++) {
            hour.add(j < 10 ? "0" + j : "" + j);
        }
        hour.add("00");
        return hour;
    }

    /**
     * 早晨小时数
     *
     * @return
     */
    public static List<String> getMornHour() {
        List<String> hour = new ArrayList();
        for (int j = 4; j <= 10; j++) {
            hour.add(j < 10 ? "0" + j : "" + j);
        }
        return hour;
    }


    /**
     * 分钟
     *
     * @return
     */
    public static List<String> getMinute() {
        List<String> m = new ArrayList();
        for (int j = 0; j <= 59; j += 1) {
            m.add(j < 10 ? "0" + j : "" + j);
        }
        return m;
    }

    /**
     * 分钟
     *
     * @return
     */
    public static List<String> getMinutes() {
        List<String> m = new ArrayList();
        for (int j = 1; j <= 24; j++) {
            m.add(String.format("%02d", j * 5));
        }
        return m;
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static String add(String v1, String v2) {
        BigDecimal b1;
        try {
            b1 = new BigDecimal(v1);
        } catch (NumberFormatException e) {
            b1 = new BigDecimal("0");
        }
        BigDecimal b2;
        try {
            b2 = new BigDecimal(v2);
        } catch (NumberFormatException e) {
            b2 = new BigDecimal("0");
        }
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static String subtract(String v1, String v2) {
        BigDecimal b1;
        try {
            b1 = new BigDecimal(v1);
        } catch (NumberFormatException e) {
            b1 = new BigDecimal("0");
        }
        BigDecimal b2;
        try {
            b2 = new BigDecimal(v2);
        } catch (NumberFormatException e) {
            b2 = new BigDecimal("0");
        }
        return b1.subtract(b2).toString();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1;
        try {
            b1 = new BigDecimal(v1);
        } catch (NumberFormatException e) {
            b1 = new BigDecimal("0");
        }
        BigDecimal b2;
        try {
            b2 = new BigDecimal(v2);
        } catch (NumberFormatException e) {
            b2 = new BigDecimal("0");
        }
        return b1.multiply(b2).toString();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由 scale 参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static String divide(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1;
        try {
            b1 = new BigDecimal(v1);
        } catch (NumberFormatException e) {
            b1 = new BigDecimal("0");
        }
        BigDecimal b2;
        try {
            b2 = new BigDecimal(v2);
        } catch (NumberFormatException e) {
            b2 = new BigDecimal("0");
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }


    /**
     * 判断小数点后2位的数字的正则表达式
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([1]{1}))(\\.(\\d){0,2})?$");
        Matcher match = pattern.matcher(str);
        return match.matches();
    }



    /**
     * 根据步数获和体重取卡路里
     *
     * @param steps
     * @param weight
     * @return
     */
    public static double getCal(int steps, String weight) {
        return round(multiply(add("0.005895", multiply("0.000693", subtract(weight, "15"))), steps + ""), 2);
    }

    /**
     * 根据步数身高和性别获取卡距离
     *
     * @param steps
     * @param height
     * @param sex
     * @return
     */
    public static double getDistance(int steps, String height, int sex) {
        String sexValue = "0.413";
        if (sex == 1) {
            sexValue = "0.415";
        }
        return round(multiply(divide(multiply(sexValue, height), "100", 2), steps + ""), 2);
    }


    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b;
        try {
            b = new BigDecimal(v);
        } catch (NumberFormatException e) {
            b = new BigDecimal("0");
        }
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * @param length
     * @return
     */
    public static String getZeroizeStringByLength(int length) {
        return String.format("%0" + length + "d", 0);
    }

    /**
     * 返回 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getDate10(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }




    /**
     * 判断字符串长度，并且判断是否为16进制数据
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str.length() != CommonConstant.ZSC_MAC_LENGTH) {
            return false;
        }
        try {
            Long.parseLong(str, 16);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 分解mac地址
     *
     * @param resultString
     * @return
     */
    public static String parseString(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = (resultString.length() / 2); i > 0; i--) {
            result.append(resultString.substring(2 * i - 2, 2 * i));
            if (i > 1) {
                result.append(":");
            }
        }
        return result.toString();
    }

    /**
     * 解析mac地址
     *
     * @param resultString
     * @return
     */
    public static String parseMac(String resultString) {

        if (TextUtils.isEmpty(resultString)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= resultString.length() / 3; i++) {
            result.append(resultString.substring(3 * i, 3 * i + 2));
        }
        return result.toString();
    }

    /**
     * 解析mac地址
     *
     * @param resultString
     * @return
     */
    public static String stringPas(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = (resultString.length() / 2); i > 0; i--) {
            result.append(resultString.substring(2 * i - 2, 2 * i));
        }
        return result.toString();
    }

    /**
     * 判断微信是否安装可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 加密
     *
     * @param bytes
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String encryptDES(byte[] bytes, String encryptKey) throws Exception {

        Log.e("bytes", "bytes == " + Arrays.toString(bytes) + "字符串" + Byte2Hex(bytes));
        byte[] tmep = new byte[8];
        byte[] result = new byte[8];
        // TODO 补够8整数字节
        int mod = bytes.length % 8;
        int length = mod == 0 ? bytes.length : (bytes.length + 8 - mod);
        byte[] byteDate = new byte[length];
        System.arraycopy(bytes, 0, byteDate, 0, bytes.length);
        Log.e("byteDate", "byteDate == " + Arrays.toString(byteDate) + "补够８字节数组字符串" + Byte2Hex(byteDate));
        //TODO 按每8个字节与后8字节异或结果继续和后8字节异或处理
        System.arraycopy(byteDate, 0, result, 0, 8);
        for (int j = 0; j < 8; j++) {
            System.arraycopy(byteDate, 8 * (1 + j), tmep, 0, 8);
            for (int i = 0; i < 8; i++) {
                if (result[i] == tmep[i]) result[i] = 0;
                else result[i] = 1;
            }
            Log.e("result", "result == " + Arrays.toString(result) + "疑惑结果８字节数组字符串" + Byte2Hex(result));
        }
        //TODO 异或处理结果的8字节用工作密钥加密
        SecretKeySpec key = new SecretKeySpec(Hex2Byte(encryptKey.getBytes()), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(byteDate);
        Log.e("encryptedData", "encryptedData == " + Arrays.toString(encryptedData) + "解密结果字符串" + Byte2Hex(encryptedData));
        return Byte2Hex(encryptedData);//Base64.encode(encryptedData);
    }

    /**
     * 解密
     *
     * @param decryptString
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(Hex2Byte(decryptKey.getBytes()), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(Hex2Byte(decryptString.getBytes()));
        return Byte2Hex(decryptedData);
    }


    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    public static String Byte2Hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    public static byte[] Hex2Byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }



    /**
     * 补够16位字符串 后面补0
     *
     * @param str
     * @return
     */
    public static String pas16(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        if (str.length() % 16 != 0) {
            int m = 16 - str.length() % 16;
            for (int i = 0; i < m; i++) {
                strBuilder.append("0");
            }
        }
        return strBuilder.toString();
    }

    /**
     * 补够16位字符串 后面补0
     *
     * @param str
     * @return
     */
    public static String pas1680(String str) {
        StringBuilder strBuilder = new StringBuilder(str);
        if (str.length() % 16 != 0) {
            int m = 16 - str.length() % 16;
            for (int i = 0; i < m; i++) {

                if (i == 0)
                    strBuilder.append("8");
                else
                    strBuilder.append("0");
            }
        }
        return strBuilder.toString();
    }

    public static void putSharePreference(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CommonConstant.OPTION_MODE_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putFloatSharePreference(Context context, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(CommonConstant.OPTION_MODE_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static boolean getSharePreference(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CommonConstant.OPTION_MODE_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static float getShareFloatPreference(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CommonConstant.OPTION_MODE_SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, 0.0f);
    }

    public static boolean checkPackage(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static void saveFile(Context context, String filename, String content) {
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatFloat2(String balance) {
        if (balance == null || balance.equals("")) balance = "0.00";
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(Double.parseDouble(balance));
    }


}
