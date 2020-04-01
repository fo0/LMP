package com.fo0.lmp.ui.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.util.Strings;

import com.fo0.lmp.ui.enums.EHostProperty;
import com.fo0.lmp.ui.utils.Random;
import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = { "id" })
public class HostProperty implements Serializable {

	private static final long serialVersionUID = -1838524764516151341L;

	@Builder.Default
	private String id = Random.generateUuid();

	@Builder.Default
	private Map<String, String> properties = Maps.newHashMap();

	public String getManagedProperty(EHostProperty property) {
		if (MapUtils.isEmpty(properties))
			return Strings.EMPTY;

		return properties.get(property.name());
	}
}
