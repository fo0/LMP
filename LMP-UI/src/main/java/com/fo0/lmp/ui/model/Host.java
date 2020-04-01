package com.fo0.lmp.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "address", "port", "username" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Host {

	private String label;
	private String address;
	@Builder.Default
	private int port = 22;

	@Builder.Default
	private String username = "root";

	private String password;
	private boolean privateKey;
	private Key key;

	@Builder.Default
	private String os = "";

	@Builder.Default
	private String distro = "";

	@Builder.Default
	private String hostname = "";

	@Builder.Default
	private String version = "";

	@Builder.Default
	private boolean active = true;

	@Builder.Default
	private boolean reachable = false;

}
