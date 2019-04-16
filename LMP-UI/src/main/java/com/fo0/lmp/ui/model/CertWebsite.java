package com.fo0.lmp.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "url", "port" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertWebsite {
	
	
	private String label;
	private String url;
	@Builder.Default
	private int port = 443;

	@Builder.Default
	private boolean active = true;
	
	@Builder.Default
	private boolean status = false;
	
	private int daysleft;
	
	private Host host;
	private Action action;

}
