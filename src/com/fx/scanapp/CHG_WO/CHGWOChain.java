package com.fx.scanapp.CHG_WO;

import com.fx.scanapp.ChainRespon;
import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CHG_CLS.CommandCLS;
import com.fx.scanapp.CHG_CLS.CommandGetMaterialPreparation;
import com.fx.scanapp.CHG_CLS.CommandStationno;
import com.fx.scanapp.CHG_KP.CommandCheckPermission;

public class CHGWOChain extends ChainRespon{
	private TaskNode head;// »ŒŒÒ¡¥Õ∑
	
	public CHGWOChain() {
		build();
	}
	
	@Override
	public void build() {
		TaskNode CLS = new CommandCLS();
		TaskNode checkpermission = new CommandCheckPermission();
		TaskNode getMaterialPreparationchgwo = new CommandGetMaterialPreparationCHGWO();
		TaskNode checkstationchgwo=new CommandStationnoCHGWO();
		CLS.setTaskLevel(1);
		checkpermission.setTaskLevel(2);
		getMaterialPreparationchgwo.setTaskLevel(3);
		checkstationchgwo.setTaskLevel(4);
		head = CLS;
		CLS.setnext(checkpermission);
		checkpermission.setnext(getMaterialPreparationchgwo);
		getMaterialPreparationchgwo.setnext(checkstationchgwo);
		Machine.cmand_status = 1;
		
	}

	@Override
	public TaskNode getChain() {
		return head;
	}

}
