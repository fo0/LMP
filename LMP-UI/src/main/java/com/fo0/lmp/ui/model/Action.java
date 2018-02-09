package com.fo0.lmp.ui.model;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "id" })
@Builder
public class Action {

	@Builder.Default
	private String id = RandomStringUtils.randomAlphanumeric(10);

	@Builder.Default
	private String command;

	@Builder.Default
	private String description;

	@Builder.Default
	private boolean active = true;

}
