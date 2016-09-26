package com.fx.scanapp.CHG_CLS;

import com.fx.scanapp.ChainRespon;
import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.Init.CommandInit;
import com.fx.scanapp.Init.CommandRight;
import com.fx.scanapp.Init.CommandTab;

public class CLSChain extends ChainRespon{
	private TaskNode head;//»ŒŒÒ¡¥Õ∑
	
	public CLSChain() {
		build();
	}
	@Override
	public void build() {
		TaskNode cls = new CommandCLS();
		cls.setTaskLevel(1);
		head = cls;
		Machine.cmand_status = 1;
		
	}

	@Override
	public TaskNode getChain() {
		return head;
	}

}
