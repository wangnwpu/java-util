/**
 * 工具类，实现日志记录功能
 * @author wangchuan
 */
package com.cwang.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLog {
	
	private static String logPath = "LogFile";	//目录
	
	private static String suffix = ".log";	//扩展名
	
	/**
	 * 记录日志，以文件的形式存储，文件名为日期
	 * @param loginfo	日志信息
	 */
	public static void doMyLog(String loginfo){		
		Date date = new Date();
		String fileName = MyLog.logPath + (new SimpleDateFormat("yyyyMMdd")).format(date) + MyLog.suffix;	//文件名	
		String dayTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);			
		String writeStr = "<--------------" + dayTime + "-------------->" + "\n" + loginfo + "\n";		
		
		try {			
			File logFile = new File(fileName);
			if(!logFile.exists()){
				logFile.createNewFile();
			}			
			FileOutputStream out=new FileOutputStream(logFile,true);
			out.write(writeStr.getBytes("utf-8"));
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	/*
	 * getters and setters
	 */	
	public static String getLogPath() {
		return logPath;
	}

	public static void setLogPath(String logPath) {
		MyLog.logPath = logPath;
	}

	public static String getSuffix() {
		return suffix;
	}

	public static void setSuffix(String suffix) {
		MyLog.suffix = suffix;
	}

}
