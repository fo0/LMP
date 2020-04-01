package com.fo0.lmp.ui.collector.hostinfo;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fo0.lmp.ui.collector.interfaces.ICollector;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.ssh.SSHClient;
import com.fo0.lmp.ui.utils.Utils;

import lombok.NonNull;

public class HostInfoCollector implements ICollector<HostInfo> {

	private Host host;
	private HostInfo info = HostInfo.builder().build();

	public HostInfoCollector(@NonNull Host host) {
		this.host = host;
	}

	@Override
	public void collect() {
		Map<String, String> lsb = collectLsbReleaseInfo(host);
		if (MapUtils.isNotEmpty(lsb)) {
			info.setCodeName(lsb.get("Codename"));
			info.setDescription(lsb.get("Description"));
			info.setDistributor(lsb.get("Distributor ID"));
			info.setVersion(lsb.get("Release"));
		}

		info.setHostname(collectHostInformation(host));
		info.setOperatingSystem(collectUnameInfo(host));
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

	public String collectHostInformation(Host host) {
		SSHClient shell = createClient(host);
		return StringUtils.trimToEmpty(shell.commandPlain("hostname -f"));
	}

	public String collectUnameInfo(Host host) {
		SSHClient shell = createClient(host);
		return StringUtils.trimToEmpty(shell.commandPlain("uname"));
	}

	public Map<String, String> collectLsbReleaseInfo(Host host) {
		SSHClient shell = createClient(host);
		String rs = shell.commandPlain("lsb_release -a");
		String[] splitted = StringUtils.split(rs, "\n");
		return Arrays.asList(splitted).stream().filter(e -> e.contains(":")).map(e -> {
			String[] spl = StringUtils.split(Utils.removeEscapeCharacters(e), ":");
			return Pair.of(StringUtils.trimToEmpty(spl[0]), StringUtils.trimToEmpty(spl[1]));
		}).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}

	@Override
	public Optional<HostInfo> getResult() {
		return Optional.of(info);
	}

}
