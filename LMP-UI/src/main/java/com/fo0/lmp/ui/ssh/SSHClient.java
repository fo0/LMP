package com.fo0.lmp.ui.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import com.fo0.lmp.ui.data.KeyManager;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.interfaces.InputListener;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.Key;
import com.fo0.logger.LOGSTATE;
import com.fo0.logger.Logger;
import com.jcabi.ssh.Shell;
import com.jcabi.ssh.Ssh;
import com.jcabi.ssh.SshByPassword;

public class SSHClient {

	private Host host = null;
	private Key key = null;

	private Shell shell = null;

	private InputListener<Character> inputListener;
	private DataListener<String> outputListener;
	private DataListener<String> errorListener;

	public SSHClient(Host host) {
		this.host = host;
	}

	public void connect() throws Exception {
		if (host.getKey() != null) {
			Set<Key> myKeys = KeyManager.load();
			for (Key keyy : myKeys) {
				if (keyy.getLabel().contains(host.getKey().getLabel())) {
					key = keyy;
					break;
				}
			}
			shell = new Ssh(host.getAddress(), host.getPort(), host.getUsername(), key.getPrivateKey());
		} else {

			shell = new SshByPassword(host.getAddress(), host.getPort(), host.getUsername(), host.getPassword());
		}
	}

	public Shell getShell() {
		return shell;
	}

	public void action(ELinuxActions action) {
		command(action.getCmd());
	}

	public void command(String cmd, InputListener<Character> inputListener, DataListener<String> outputListener,
			DataListener<String> errorListener) {
		if (shell == null) {
			Logger.log.error(LOGSTATE.FAILED + "connection not found");
			return;
		}

		try {
			shell.exec(cmd, new InputStream() {

				@Override
				public int read() throws IOException {
					if (inputListener != null)
						return inputListener.event();
					else
						return 0;
				}
			}, new OutputStream() {

				@Override
				public void write(int b) throws IOException {
					char c = (char) b;
					// System.out.println(c);
					outputListener.event(String.valueOf(c));
				}
			}, new OutputStream() {

				@Override
				public void write(int b) throws IOException {
					char c = (char) b;
					// System.out.println(c);
					errorListener.event(String.valueOf(c));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			if (errorListener != null)
				errorListener.event(e.getMessage());
		}
	}

	public void command(String cmd) {
		command(cmd, inputListener, outputListener, errorListener);
	}

	public String commandPlain(String cmd) {
		String plain = null;
		try {
			plain = new Shell.Plain(shell).exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plain;
	}

	public void close() {
		if (shell != null) {
			try {
				shell = null;
			} catch (Exception e) {
			}
		}
	}

	public void test() {
		if (shell == null) {
			Logger.log.error(LOGSTATE.FAILED + "connection not found");
			return;
		}

		command("echo 'max lol'");
	}

	public InputListener<Character> getInputListener() {
		return inputListener;
	}

	public void setInputListener(InputListener<Character> inputListener) {
		this.inputListener = inputListener;
	}

	public DataListener<String> getOutputListener() {
		return outputListener;
	}

	public void setOutputListener(DataListener<String> outputListener) {
		this.outputListener = outputListener;
	}

	public DataListener<String> getErrorListener() {
		return errorListener;
	}

	public void setErrorListener(DataListener<String> errorListener) {
		this.errorListener = errorListener;
	}

}
