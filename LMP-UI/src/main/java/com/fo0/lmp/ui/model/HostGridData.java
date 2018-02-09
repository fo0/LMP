package com.fo0.lmp.ui.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "host" })
@Builder
public class HostGridData {

	private Host host;
	private HostConnector connector;

}
