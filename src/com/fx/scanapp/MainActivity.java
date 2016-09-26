package com.fx.scanapp;


import com.zxing.activity.CaptureActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ResponsibiltyFactory factory;
	Machine machine;
	Watcher watcher;
	
	TextView user;
	TextView masterID;
	TextView inputmsg;
	TextView outputmsg;
	Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        oninit();
        machine=Machine.getInstance();
        watcher=new Watcher();
        watcher.addelements(user,masterID,inputmsg,outputmsg);
        machine.addObserver(watcher);
        factory=ResponsibiltyFactory.getInstance(); 
        factory.creatProduct("ACTION-INIT").send(machine.cmand_status, "ACTION_INIT"); 
        btn_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				scanning();
				
			}
		});
    }
    
    public void oninit(){
        user=(TextView) findViewById(R.id.user);
        masterID=(TextView) findViewById(R.id.masterID);
        inputmsg=(TextView) findViewById(R.id.inputmsg);
        outputmsg=(TextView) findViewById(R.id.nextdo);
        btn_scan=(Button) findViewById(R.id.btn_scan);
    }
    
    
	private void scanning(){
		Intent intent = new Intent(this,CaptureActivity.class);		
		startActivityForResult(intent, 0);							
	}

    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		if(resultCode == RESULT_OK){		
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");	
			factory.creatProduct(scanResult).send(machine.cmand_status, scanResult); 
			//inputmsg.setText(scanResult);
		}
	} 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
