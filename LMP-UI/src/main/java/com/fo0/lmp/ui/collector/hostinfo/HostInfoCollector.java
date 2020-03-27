package com.fo0.lmp.ui.collector.hostinfo;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.ssh.SSHClient;
import com.fo0.lmp.ui.utils.Utils;

import lombok.Getter;
import lombok.NonNull;

public class HostInfoCollector {

	@Getter
	private Host host;

	public HostInfoCollector(@NonNull Host host) {
		this.host = host;
	}

	public HostInfoCollector withCollect() {
		host.setOs(getUname(host));
		host.setHostname(getHostInformation(host));
		HostInfo info = getLsb(host);
		host.setDistro(info.getDistributor());
		host.setVersion(info.getVersion());
		return this;
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

	public String getHostInformation(Host host) {
		SSHClient shell = createClient(host);
		return StringUtils.trimToEmpty(shell.commandPlain("hostname -f"));
	}

	public String getUname(Host host) {
		SSHClient shell = createClient(host);
		return StringUtils.trimToEmpty(shell.commandPlain("uname"));
	}

	public HostInfo getLsb(Host host) {
		try {
			SSHClient shell = createClient(host);
			String rs = shell.commandPlain("lsb_release -a");
			String[] splitted = StringUtils.split(rs, "\n");
			Map<String, String> map = Arrays.asList(splitted).stream().filter(e -> e.contains(":")).map(e -> {
				String[] spl = StringUtils.split(Utils.removeEscapeCharacters(e), ":");
				return Pair.of(StringUtils.trimToEmpty(spl[0]), StringUtils.trimToEmpty(spl[1]));
			}).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
			return new HostInfo(map);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return HostInfo.builder().build();
	}

}
