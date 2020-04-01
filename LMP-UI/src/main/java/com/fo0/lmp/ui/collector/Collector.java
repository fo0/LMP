package com.fo0.lmp.ui.collector;

import com.fo0.lmp.ui.collector.hostinfo.HostInfoCollector;
import com.fo0.lmp.ui.collector.network.NetworkCollector;
import com.fo0.lmp.ui.model.Host;

public class Collector {

	public static void collect(Host host) {
		new HostInfoCollector(host).collectAndSaveResult();
		new NetworkCollector(host).collectAndSaveResult();
	}
	
}
