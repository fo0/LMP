package com.fo0.lmp.ui.enums;

public enum ENode {

	Core("com.fo0.fcf.architecture.interfaces.IApiRmiCore"),

	Agent("com.fo0.fcf.architecture.interfaces.IApiRmiAgent"),

	Daemon("com.fo0.fcf.architecture.interfaces.IApiRmiDaemon");

	private String stub;

	private ENode(String stub) {
		this.stub = stub;
	}

	public String getStub() {
		return stub;
	}
}
