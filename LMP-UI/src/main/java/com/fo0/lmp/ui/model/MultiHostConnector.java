package com.fo0.lmp.ui.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.ssh.SSHClient;

public class MultiHostConnector {

	private volatile StringBuffer output = new StringBuffer();
	private Set<Host> list;
	private DataListener<SSHClient> cmd;
	private DataListener<HostConnector> currentHost;
	private boolean threaded;

	private boolean start = false;
	private boolean interrupt = false;

	private Thread thread = null;

	private List<HostConnector> connectors = new ArrayList<HostConnector>();

	public MultiHostConnector(Set<Host> queue, DataListener<SSHClient> cmd, DataListener<HostConnector> currentHost,
			boolean threaded, boolean start) {
		this.list = queue;
		this.cmd = cmd;
		this.currentHost = currentHost;
		this.threaded = threaded;
		if (start)
			start();
	}

	public MultiHostConnector(Set<Host> queue, DataListener<SSHClient> cmd, DataListener<HostConnector> currentHost,
			boolean threaded) {
		this(queue, cmd, currentHost, threaded, false);
	}

	public MultiHostConnector(Set<Host> queue, DataListener<SSHClient> cmd, boolean threaded) {
		this(queue, cmd, null, threaded);
	}

	public void start() {
		if (!interrupt)
			if (!start) {
				start = true;
				thread = new Thread(() -> {
					Set<Host> queuedHosts = new HashSet<Host>(list);
					queuedHosts.stream().forEach(e -> {
						try {
							if (start && !interrupt) {
								HostConnector connector = new HostConnector(e, cmd, null, threaded, false);
								connectors.add(connector);
								connector.setPush(push -> {
									this.output.append(push);
									this.currentHost.event(connector);
								});
								connector.start();
							}
						} catch (Exception e2) {
						}

					});
				});
				thread.start();
			}

	}

	public String getOutput() {
		String tmp = output.toString();
		output.setLength(0);
		return tmp;
	}

	public void setCurrentHost(DataListener<HostConnector> currentHost) {
		this.currentHost = currentHost;
	}

	public void close() {
		try {

			connectors.forEach(e -> {
				try {
					e.close();
				} catch (Exception e2) {
				}
			});

		} catch (Exception e) {
		}
	}

	public void stop() {
		try {
			start = false;
			interrupt = true;
			try {
				list.clear();
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				if (thread != null) {
					thread.interrupt();
					thread = null;
				}
			} catch (Exception e) {
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public boolean isRunning() {
		return start;
	}
}
