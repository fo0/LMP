package com.fo0.lmp.ui.enums;

public enum ELinuxActions {

	APT_UPDATE("apt-get update"),

	APT_UPDATE_AND_DIST_UPGRADE("apt-get update && apt-get dist-upgrade -f -y"),

	APT_DIST_UPGRADE("apt-get dist-upgrade -f -y"),

	APT_AUTOREMOVE("apt-get autoremove -f -y"),

	PING("ping localhost -c 1"),

	DETECT_OS("lsb_release -is"),
	
	CUSTOM("");

	private String cmd;

	private ELinuxActions(String cmd) {
		this.cmd = cmd;
	}

	public String getCmd() {
		return this.cmd;
	}

}
