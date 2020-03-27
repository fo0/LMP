package com.fo0.lmp.ui.collector.hostinfo;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostInfo implements Serializable {

	private static final long serialVersionUID = -7318350920699036164L;

	private String distributor;
	private String description;
	private String version;
	private String codeName;

	public HostInfo(Map<String, String> lsb_release_splitted) {
		distributor = lsb_release_splitted.get("Distributor ID");
		description = lsb_release_splitted.get("Description");
		version = lsb_release_splitted.get("Release");
		codeName = lsb_release_splitted.get("Codename");
	}

}
