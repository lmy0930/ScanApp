package com.fx.scanapp.CHG_KP;

import com.fx.scanapp.ChainRespon;
import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.Init.CommandInit;
import com.fx.scanapp.Init.CommandRight;
import com.fx.scanapp.Init.CommandTab;

public class KPChain extends ChainRespon {
	private TaskNode head;// »ŒŒÒ¡¥Õ∑

	public KPChain() {
		build();
	}

	@Override
	public void build() {
		TaskNode Kp = new CommandKP();
		TaskNode checkpermission = new CommandCheckPermission();
		TaskNode checkSupplyEmp = new CommandCheckSupplyEMP();
		TaskNode getTrsnMasterial=new CommandGetTrsnMaterial();
		TaskNode checkstation=new CommandCHGKPCLS();
		Kp.setTaskLevel(1);
		checkpermission.setTaskLevel(2);
		checkSupplyEmp.setTaskLevel(3);
		getTrsnMasterial.setTaskLevel(4);
		checkstation.setTaskLevel(5);
		head = Kp;
		Kp.setnext(checkpermission);
		checkpermission.setnext(checkSupplyEmp);
		checkSupplyEmp.setnext(getTrsnMasterial);
		getTrsnMasterial.setnext(checkstation);
		Machine.cmand_status = 1;

	}

	@Override
	public TaskNode getChain() {

		return head;
	}

}
