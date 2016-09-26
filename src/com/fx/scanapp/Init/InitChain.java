package com.fx.scanapp.Init;

import com.fx.scanapp.ChainRespon;
import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;

public class InitChain extends ChainRespon {
	private TaskNode head;//������ͷ

	public InitChain() {
		build();
	}

	@Override
	public void build() {// ����������
		TaskNode init = new CommandInit();
		TaskNode Right = new CommandRight();
		TaskNode Tab = new CommandTab();
		init.setTaskLevel(1);
		Right.setTaskLevel(2);
		Tab.setTaskLevel(3);
		head = init;
		init.setnext(Right);
		Right.setnext(Tab);
		Machine.cmand_status = 1;
	}

	@Override
	public TaskNode getChain() {//���������ͷ
		return head;
	}

}
