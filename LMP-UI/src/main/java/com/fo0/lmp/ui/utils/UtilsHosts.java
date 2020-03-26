package com.fo0.lmp.ui.utils;

import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.ssh.SSHClient;

public class UtilsHosts {

	public static Host getHostInformation(Host host) {

		SSHClient shell = new SSHClient(host);
		try {
			shell.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		host.setLabel(shell.commandPlain("hostname -f"));
		host.setOs(shell.commandPlain(ELinuxActions.DETECT_OS.getCmd()));
		return host;

	}

}
