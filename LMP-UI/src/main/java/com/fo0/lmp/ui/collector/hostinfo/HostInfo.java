package com.fo0.lmp.ui.collector.hostinfo;

import java.io.Serializable;

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

	private String operatingSystem;
	private String hostname;
	private String distributor;
	private String description;
	private String version;
	private String codeName;

}
