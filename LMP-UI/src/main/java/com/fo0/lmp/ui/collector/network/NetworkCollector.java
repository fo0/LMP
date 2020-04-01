package com.fo0.lmp.ui.collector.network;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fo0.fcf.logger.LOGSTATE;
import com.fo0.fcf.logger.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fo0.lmp.ui.collector.interfaces.ICollector;
import com.fo0.lmp.ui.enums.EHostProperty;
import com.fo0.lmp.ui.manager.HostPropertyManager;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.ssh.SSHClient;
import com.google.common.collect.Maps;

import lombok.NonNull;

public class NetworkCollector implements ICollector<Map<String, String>> {

	private Host host;
	private Map<String, String> map = Maps.newHashMap();

	public NetworkCollector(@NonNull Host host) {
		this.host = host;
	}

	public static void main(String[] args) {
		new NetworkCollector(Host.builder().address("10.0.0.203").username("root").password("max123").build())
				.collect();
	}

	@Override
	public void collect() {
		SSHClient c = createClient(host);
		String ifconfig = c.commandPlain(
				"ip addr show | awk '/^[^ ]/ && NR!=1 {print \"\"} {printf \"%s\", $0} END {print \"\"}' | awk '{ print $2$19$17 }'");
		// parse ifconfig to list of interfaces
		List<String> addresses = Arrays.asList(StringUtils.split(ifconfig, "\n"));

		addresses.stream().forEach(e -> {
			try {
				String iface = e.split(":", 2)[0];
				String address = e.split(":", 2)[1].split("/")[0];
				String mac = e.split(":", 2)[1].split("/")[1];
				map.put(String.format("IPv4 %s", iface), address);
				map.put(String.format("MAC %s", iface), mac);
			} catch (Exception e2) {
				Logger.log.error(LOGSTATE.FAILED + "to parse inteface output", e2);
			}
		});

		map.entrySet().stream().forEach(System.out::println);
	}

	public SSHClient createClient(Host host) {
		SSHClient shell = new SSHClient(host);
		try {
			shell.connect();
			return shell;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Optional<Map<String, String>> getResult() {
		return Optional.of(map);
	}

	@Override
	public void mergeAndSave() {
		Logger.log.debug(LOGSTATE.ADD + "collected network properties to host: " + host.getId());
		map.entrySet().forEach(e -> {
			HostPropertyManager.addPropertyToHost(host.getId(), e.getKey(), e.getValue());
		});
	}

}
