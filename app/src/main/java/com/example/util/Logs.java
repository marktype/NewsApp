package com.example.util;

import android.util.Log;

public class Logs {

	public static final String tag = "tag";
	public static boolean flag = true; // if==true 显示日志; else 关闭日志.
	public static void v(String message){
		if (flag) {
			Log.v(tag, message);
		}
	}
	public static void d(String message){
		if (flag) {
			Log.d(tag, message);
		}
	}
	public static void e(String message){
		if (flag) {
			Log.e(tag, message);
		}
	}
	public static void i(String message){
		if (flag) {
			Log.i(tag, message);
		}
	}
	public static void w(String message){
		if (flag) {
			Log.w(tag, message);
		}
	}
	public static void wtf(String message){
		if (flag) {
			Log.wtf(tag, message);
		}
	}
}
