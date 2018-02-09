package com.fo0.lmp.ui.config;

import java.nio.file.Paths;
import java.util.Properties;

import com.fo0.logger.LOGSTATE;
import com.fo0.logger.Logger;

public class CONSTANTS {

	public static String path = "";
	public static Configuration config = null;
	private static UIConfig raw;

	public static void bootstrap(UIConfig userConfig) {
		path = System.getProperty("jboss.server.config.dir") + "/LMP.properties";

		if (!Paths.get(path).toFile().exists()) {
			try {
				Paths.get(path).toFile().createNewFile();
			} catch (Exception e) {
				Logger.log.info(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties " + e);
			}
		}
		Properties DEFAULT = new Properties();

		if (userConfig.isValid()) {
			DEFAULT.setProperty(EConfig.AUTO_LOGIN.name(), userConfig.getAutoLogin());
		} else {
			DEFAULT.setProperty(EConfig.AUTO_LOGIN.name(), "false");
		}

		config = new Configuration(Paths.get(path), DEFAULT);

		raw = userConfig;
	}

	public static Configuration get() {
		return config;
	}

	public static void writeConfig(UIConfig userConfig) {
		config.addProperty(EConfig.AUTO_LOGIN, userConfig.getAutoLogin());
	}

	public static UIConfig getConfig() {
		return new UIConfig(get().getProperty(EConfig.AUTO_LOGIN));
	}
}
