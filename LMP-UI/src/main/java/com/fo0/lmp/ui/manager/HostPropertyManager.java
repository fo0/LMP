package com.fo0.lmp.ui.manager;

import com.fo0.lmp.ui.data.HostPropertyLoader;
import com.fo0.lmp.ui.enums.EHostProperty;
import com.fo0.lmp.ui.model.HostProperty;

public class HostPropertyManager {

	public static void addManagedPropertyToHost(String hostId, EHostProperty key, String value) {
		addPropertyToHost(hostId, key.name(), value);
	}

	public static void addPropertyToHost(String hostId, String key, String value) {
		HostProperty p = getPropertiesForHost(hostId);
		p.getProperties().put(key, value);
		HostPropertyLoader.save(p);
	}

	public static HostProperty getPropertiesForHost(String id) {
		return HostPropertyLoader.getHostPropertyByIdOrCreate(id);
	}

	public static void saveProperty(HostProperty hostProperty) {
		HostPropertyLoader.save(hostProperty);
	}

	public static void deleteProperty(HostProperty hostProperty) {
		HostPropertyLoader.delete(hostProperty);
	}

	public static void deletePropertyById(String id) {
		HostPropertyLoader.deleteById(id);
	}
}
