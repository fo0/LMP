package com.fo0.lmp.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "label" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Key {

	private String label;

	private String privateKey;
	private String publicKey;

}
