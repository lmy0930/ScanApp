package com.fx.scanapp.fileAnalyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.os.Environment;

public class FileIO {
	// ������д��SD��
	static public void writeSDcard(String str) throws SDException{
		try {
			// �ж��Ƿ����SD��
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// ��ȡSD����Ŀ¼
				File file = new File(Environment.getExternalStorageDirectory()
						.getCanonicalPath() + "/test.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fW = new FileOutputStream(file);
				fW.write(str.getBytes());
				fW.close();
			} else {
				throw new SDException("SD������");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public String readSDcard()throws SDException {
		StringBuffer str = new StringBuffer();
		try {
			// �ж��Ƿ����SD
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File file = new File(Environment.getExternalStorageDirectory()
						.getCanonicalPath() + "/test.txt");
				// �ж��Ƿ���ڸ��ļ�
				if (!file.exists()) {
					file.createNewFile();
				}
				else
				{
					// ���ļ�������
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
				throw new SDException("SD�������ڣ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}
