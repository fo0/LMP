package com.fo0.lmp.ui.model;

import com.fo0.lmp.ui.utils.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "id" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Host {

	@Builder.Default
	private String id = Random.generateUuid();

	private String label;
	private String address;
	@Builder.Default
	private int port = 22;
	@Builder.Default
	private String username = "root";
	private String password;

	@Builder.Default
	private boolean active = true;

	@Builder.Default
	private boolean reachable = false;

	// KEY AUTHENTICATION
	private boolean privateKey;
	private Key key;
}
