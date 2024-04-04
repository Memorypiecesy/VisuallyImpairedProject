package com.example.visuallyimpairedproject.utils;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {

    public static void setStatusBar(AppCompatActivity appCompatActivity){
        //系统版本号判断，5.0以上系统才支持改变系统状态栏风格功能
        if (Build.VERSION.SDK_INT >= 21) {
            //拿到当前活动的DecorView
            View decorView = appCompatActivity.getWindow().getDecorView();//拿到当前活动的DecorView
            //第一个参数表示活动的布局会显示在状态栏上面，第二个参数表示当状态栏的背景为浅色系时，状态栏变为深色系
            //一定要用"|"来写两个值，如果分开用两行代码写，则后面设置的会覆盖前面的，不能实现同时一起的效果
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            appCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //注意要清除 FLAG_TRANSLUCENT_STATUS flag
            appCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //setStatusBarColor()方法将状态栏设置为透明色
            appCompatActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
