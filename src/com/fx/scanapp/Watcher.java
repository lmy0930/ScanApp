package com.fx.scanapp;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class Watcher implements Observer{
	TextView user;
	TextView masterID;
	TextView inputString;
	TextView nextdo;

	@Override
	public void update(Observable arg0, Object arg1) {//ÐÞ¸ÄUI
		// TODO Auto-generated method stub
		Machine mach=Machine.getInstance();
		user.setText(mach.user);
		masterID.setText(mach.masterID);
		inputString.setText(mach.inputmsg);
		nextdo.setText(mach.nextdo);

	}
	
	public void addelements(TextView user,TextView masterID,TextView inputString,TextView nextdo)
	{
		this.user=user;
		this.masterID=masterID;
		this.inputString=inputString;
		this.nextdo=nextdo;
	}

}
