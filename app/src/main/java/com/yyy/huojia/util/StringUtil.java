package com.yyy.huojia.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.yyy.huojia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtil {
    public final static int DATETYPE = 1;
    final static String colorFormat = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$";

    /**
     * 判断字符串是否为整型
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断颜色是否正确
     *
     * @param color
     * @return
     */
    public static boolean isColor(String color) {
        return color.matches(colorFormat);
    }

    /**
     * 判断是否粗体，0否，1是
     *
     * @param i
     * @return
     */
    public static boolean isBold(int i) {
        if (i == 0)
            return false;
        else
            return true;
    }

    /**
     * 转化单行占比为float型数据，当超过一行时只取一行长度
     *
     * @param i
     * @return
     */
    public static float isPercent(int i) {

        if (i > 100)
            i = 100;
        return (i / 100f);
    }

    /**
     * 获取时间格式，type=1时间格式为yyyy-MM-dd HH:mm
     *
     * @param date
     * @param type
     * @return
     */
    public static String getDatetime(String date, int type) throws Exception {
        if (type == 1) {
            date = date.substring(0, 16);
            date = date.replace("T", " ");
            return date;
        } else
            return date;
    }

    /**
     * 获取时间格式，type=1时间格式为yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    public static String formatDate(@NonNull String date) {
        if (date == null && date.length() < 10)
            return date;
        date = date.substring(0, 10);
        return date;

    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    /**
     * 替换字符串中的null值
     *
     * @param str
     * @return
     */
    public static String replaceNull(String str) {
        return str.replace(":null", ":\"\"");
    }

    public static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }

    public static String getTime(@Nullable Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
        if (date == null)
            date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String getDate(@Nullable Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
        if (date == null)
            date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /*获取筛选条件默认值*/
    public static String getDefaulText(Context context, String text) {
        text = TextUtils.isEmpty(text) ? "" : text;
        SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(context, context.getResources().getString(R.string.preferenceCache));

        switch (text) {
            case "UserID":
                text = (String) preferencesHelper.getSharedPreference("userid", "");
                break;
            case "UserName":
                text = (String) preferencesHelper.getSharedPreference("username", "");
                break;
            case "CurrentDate":
                text = getDate(null);
                break;
            case "CurrentDateTime":
                text = getTime(null);
                break;
            case "Departid":
                text = (String) preferencesHelper.getSharedPreference("userDepartment", "");
                break;
            case "NewGUID":
                text = new RandomGUID().toString();
                break;
            default:
                break;
        }
        return text;
    }

    /*解析lookup数据*/
    public static String getLookupData(String tables, String keyReturn, String keyShow) throws JSONException {
        JSONObject jsonObject = new JSONObject(tables);
        JSONArray jsonArray = jsonObject.optJSONArray("LookupData");
        String data = "[";
        for (int i = 0; i < jsonArray.length(); i++) {
            data = data + "{";
            data = data + "\"id\":";
            data = data + "\"";
            data = data + jsonArray.getJSONObject(i).optString(keyReturn);
            data = data + "\"";
            data = data + ",";
            data = data + "\"name\":";
            data = data + "\"";
            data = data + jsonArray.getJSONObject(i).optString(keyShow);
            data = data + "\"";
            data = data + "}";
            if (i != jsonArray.length() - 1)
                data = data + ",";
        }
        return data + "]";
    }


}
