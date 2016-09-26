package com.fx.scanapp.fileAnalyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.os.Environment;

public class FileIO {
	// 把数据写入SD卡
	static public void writeSDcard(String str) throws SDException{
		try {
			// 判断是否存在SD卡
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File file = new File(Environment.getExternalStorageDirectory()
						.getCanonicalPath() + "/test.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fW = new FileOutputStream(file);
				fW.write(str.getBytes());
				fW.close();
			} else {
				throw new SDException("SD不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public String readSDcard()throws SDException {
		StringBuffer str = new StringBuffer();
		try {
			// 判断是否存在SD
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getCanonicalPath() + "/test.txt");
				// 判断是否存在该文件
				if (!file.exists()) {
					file.createNewFile();
				}
				else
				{
					// 打开文件输入流
					FileInputStream fileR = new FileInputStream(file);
					BufferedReader reads = new BufferedReader(
							new InputStreamReader(fileR));
					String st = null;
					while ((st = reads.readLine()) != null) {
						str.append(st);
					}
					fileR.close();
				} 
			} else {
				throw new SDException("SD卡不存在！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}
