package com.fx.scanapp.SCARCITY;

import com.fx.scanapp.ChainRespon;
import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CHG_KP.CommandCHGKPCLS;
import com.fx.scanapp.CHG_KP.CommandCheckPermission;
import com.fx.scanapp.CHG_KP.CommandCheckSupplyEMP;
import com.fx.scanapp.CHG_KP.CommandGetTrsnMaterial;
import com.fx.scanapp.CHG_KP.CommandKP;

public class SCARCITYChain extends ChainRespon{
	private TaskNode head;// »ŒŒÒ¡¥Õ∑
	
	
	public SCARCITYChain() {
		build();
	}
	@Override
	public void build() {
		TaskNode SCARCITY = new CommandSCARCITY();
		TaskNode checkpermission = new CommandCheckPermission();
		TaskNode getKpNumberInSEQ = new CommandGetKpNumberInSEQ();
		TaskNode checkStation=new CommandCheckStation();

		SCARCITY.setTaskLevel(1);
		checkpermission.setTaskLevel(2);
		getKpNumberInSEQ.setTaskLevel(3);
		checkStation.setTaskLevel(4);

		head = SCARCITY;
		SCARCITY.setnext(checkpermission);
		checkpermission.setnext(getKpNumberInSEQ);
		getKpNumberInSEQ.setnext(checkStation);
		Machine.cmand_status = 1;
		
	}

	@Override
	public TaskNode getChain() {
		return head;
	}

}
