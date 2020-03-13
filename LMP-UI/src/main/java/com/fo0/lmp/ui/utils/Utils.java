package com.fo0.lmp.ui.utils;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.vaadin.artur.KeyAction;
import org.vaadin.artur.KeyAction.KeyActionListener;

import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

public class Utils {

	public static void doClick(Component c) {
		c.setId(RandomStringUtils.randomAlphabetic(10));
		Page.getCurrent().getJavaScript().execute(
				"jQuery(document).ready(function($){$(\"#" + c.getId() + " .v-filterselect-button\").click();});");
	}

	public static KeyAction addKeyListener(KeyActionListener listener, AbstractComponent component, int keycode,
			int... modifierKey) {
		KeyAction action = new KeyAction(keycode, modifierKey);
		action.addKeypressListener(listener);
		action.extend(component);
		return action;
	}

	/**
	 * call this method with getParent() <br>
	 * to close the window if not accessable
	 * 
	 * @param c
	 */
	public static void closeWindow(Component c) {
		try {
			if (c instanceof Window) {
				Window w = (Window) c;
				w.close();
			}

			if (c.getParent() != null) {
				if (c.getParent() instanceof Window) {
					closeWindow(c.getParent());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double getPercent(long total, long chunk) {
		return (100d * chunk / total) / 100;
	}

	public static boolean checkForCompatibleRmiServer(String ip, int port, String... stubs) {
		try {
			Registry reg = LocateRegistry.getRegistry(ip, port);

			if (reg == null)
				return false;

			List<String> regStubs = Stream.of(reg.list()).collect(Collectors.toList());

			try {
				UnicastRemoteObject.unexportObject(reg, true);
			} catch (Exception e) {
			}

			if (stubs == null || stubs.length == 0)
				return true;

			List<String> foundRegisteredStubs = Stream.of(stubs).filter(regStubs::contains)
					.collect(Collectors.toList());

			if (foundRegisteredStubs.size() == stubs.length) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAddressReachable(String address, int port, int timeout) {
		Socket socket = null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(address, port), timeout);
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		} finally {
			IOUtils.closeQuietly(socket);
		}
	}

}
