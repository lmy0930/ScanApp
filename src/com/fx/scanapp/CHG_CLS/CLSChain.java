package com.fx.scanapp.CHG_CLS;

import com.fx.scanapp.ChainRespon;
import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CHG_KP.CommandCHGKPCLS;
import com.fx.scanapp.CHG_KP.CommandCheckPermission;
import com.fx.scanapp.CHG_KP.CommandCheckSupplyEMP;
import com.fx.scanapp.CHG_KP.CommandGetTrsnMaterial;
import com.fx.scanapp.CHG_KP.CommandKP;
import com.fx.scanapp.Init.CommandInit;
import com.fx.scanapp.Init.CommandRight;
import com.fx.scanapp.Init.CommandTab;

public class CLSChain extends ChainRespon{
	private TaskNode head;//������ͷ
	
	public CLSChain() {
		build();
	}
	@Override
	public void build() {
		TaskNode CLS = new CommandCHGKPCLS();
		TaskNode checkpermission = new CommandCheckPermission();
		TaskNode getMaterialPreparation = new CommandGetMaterialPreparation();
		TaskNode getTrsnMasterial=new CommandGetTrsnMaterial();
		TaskNode checkstation=new CommandCHGKPCLS();
		CLS.setTaskLevel(1);
		checkpermission.setTaskLevel(2);
		getMaterialPreparation.setTaskLevel(3);
		head = CLS;
		CLS.setnext(checkpermission);
		checkpermission.setnext(getMaterialPreparation);
		Machine.cmand_status = 1;
		
	}

	@Override
	public TaskNode getChain() {
		return head;
	}

}
