package com.cmhi.log.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmhi.log.helper.encryptUtil.EncryptUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    //定义GB的计算常量
    private static final int GB = 1024 * 1024 * 1024;
    //定义MB的计算常量
    private static final int MB = 1024 * 1024;
    //定义KB的计算常量
    private static final int KB = 1024;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        textView.setText("可用存储空间：" + bytes2kb(getAvailROMSize()));

//        handler = new Handler();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "测试", Toast.LENGTH_SHORT).show();
//                Log.e("handler", "执行了");
//                handler.postDelayed(this, 2000);
//            }
//        });
    }

    //可用内部存储大小 B
    public static long getAvailROMSize() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long totalCounts = statFs.getBlockCountLong();//总共的block数
        long availableCounts = statFs.getAvailableBlocksLong(); //获取可用的block数
        long size = statFs.getBlockSizeLong(); //每格所占的大小，一般是4KB==
        return availableCounts * size;//可用内部存储大小
    }

    public static String bytes2kb(long bytes) {
        //格式化小数
        DecimalFormat format = new DecimalFormat("###.0");
        if (bytes / GB >= 1) {
            return format.format(bytes / GB) + "GB";
        } else if (bytes / MB >= 1) {
            return format.format(bytes / MB) + "MB";
        } else if (bytes / KB >= 1) {
            return format.format(bytes / KB) + "KB";
        } else {
            return bytes + "B";
        }
    }

    public void exit(View view) {
        killPackageProcesses(MainActivity.this, "com.gitv.launcher.soft");
    }


    public void killPackAge(String pageAgeName) {
        try {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(pageAgeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void killPackageProcesses(Context context, String targetPackageName) {
        try {
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method method = ActivityManager.class.getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, targetPackageName);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void btn1(View view) {


    }
    public static String areaCode = "320000";
    public static String tvMac = "EC9C32CD3C70";
    public void btn2(View view) {

        JSONObject reqDataJson = new JSONObject();
        try {
            reqDataJson.put("vCode", "");
            reqDataJson.put("areaCode", areaCode);
            reqDataJson.put("tvMac", tvMac);
            String reqdata = EncryptUtil.encryptStr(reqDataJson.toString(), areaCode);
            System.out.print("加密前数据：" + reqdata);
            String jiamiData = "l/XqJuA3UxqUfZgZHGG8Oyp0kmFi8QVe7XJUqmk4AQjglgydwV/2qBEfORdazDLZhmRrq3S3Qjv0zbCc83NGQr1EI9UoI7kY1NZ4WmwnsLT6VB17VTNJHwRvyhcHAke1STcOnD3HDTKCkBoi55Ei1QM9XuTmS61VS6Aq2iXZ8+A5j5MPDfYpVgOCdMabT8KIpPmd8JrtO8pQwIMy";
            String data = EncryptUtil.decryptionStr(jiamiData, "320000");
            System.out.print("解密数据：" + data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}