package com.fo0.lmp.ui.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.ssh.SSHClient;

public class HostConnector {

	private SSHClient client;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:sss");
	private Host host;
	private DataListener<SSHClient> ssh;
	private DataListener<String> pushInternal;
	private DataListener<String> push;
	private boolean errors;
	private boolean threaded;

	private volatile StringBuffer output = new StringBuffer();

	private boolean finished = false;

	public HostConnector(Host host, DataListener<SSHClient> ssh, boolean start) {
		this(host, ssh, false, start);
	}

	public HostConnector(Host host, DataListener<SSHClient> ssh, boolean threaded, boolean start) {
		this(host, ssh, null, threaded, start);
	}

	public HostConnector(Host host, DataListener<SSHClient> ssh, DataListener<String> push, boolean threaded,
			boolean start) {
		this.host = host;
		this.ssh = ssh;
		this.push = push;
		this.threaded = threaded;

		this.pushInternal = pto -> {
			// detect newline character and append data
			if (pto.charAt(0) == 10) {
				pto = pto + "[" + sdf.format(new Date()) + "|" + host.getLabel() + "]> ";
			}

			output.append(pto);
			// this.output += pto;

			if (this.push != null) {
				this.push.event(pto);
			}

		};

		output.append("[" + sdf.format(new Date()) + "|" + host.getLabel() + "]> ");

		if (start)
			start();
	}

	public void start() {
		System.out.println("Connect to host: " + host);
		client = new SSHClient(host);
		try {
			client.connect();
		} catch (Exception e1) {
			System.err.println("error while updating host: " + host.getAddress() + ":" + host.getPort());
			return;
		}

		client.setOutputListener(e -> {
			if (pushInternal != null)
				pushInternal.event(e);
		});

		client.setErrorListener(e -> {
			errors = true;
			if (pushInternal != null)
				pushInternal.event(e);
		});

		if (threaded) {
			new Thread(() -> {
				if (pushInternal != null)
					pushInternal.event("Starting on Host: " + host.getAddress() + ":" + host.getPort() + "\n");

				ssh.event(client);

				finished = true;
				if (pushInternal != null)
					pushInternal.event("Finished on Host: " + host.getAddress() + ":" + host.getPort() + "\n");

			}).start();
		} else {
			if (pushInternal != null)
				pushInternal.event("Starting on Host: " + host.getAddress() + ":" + host.getPort() + "\n");

			ssh.event(client);

			finished = true;
			if (pushInternal != null)
				pushInternal.event("Finished on Host:: " + host.getAddress() + ":" + host.getPort() + "\n");

		}

	}

	public String getOutput() {
		String tmp = output.toString();
		output.setLength(0);
		return tmp;
	}

	public boolean isErrors() {
		return errors;
	}

	public void setPush(DataListener<String> push) {
		this.push = push;
	}

	public Host getHost() {
		return host;
	}

	public boolean isFinished() {
		return finished;
	}

	public void close() {
		try {
			if (client != null) {
				client.close();
			}
		} catch (Exception e) {
		}
	}

}
