package com.fo0.lmp.ui.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.Properties;

import com.fo0.fcf.logger.LOGSTATE;
import com.fo0.fcf.logger.Logger;

public class Configuration {

	private Path PATH_CFG = null;
	private Properties PROPERTIES = null;

	public Configuration(Path pathOfConfigFile, Properties properties) {
		Logger.log.trace(LOGSTATE.CONFIG + "Path: " + pathOfConfigFile);
		this.PATH_CFG = pathOfConfigFile;
		this.PROPERTIES = properties;

		if (!PATH_CFG.toFile().exists()) {
			try {
				PATH_CFG.toFile().createNewFile();
				writeProperties();
			} catch (Exception e) {
				Logger.log.error(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties " + e);
			}
		} else {
			addMissingProperties();
		}

		reloadProperties();
	}

	public String getProperty(String property) {
		return PROPERTIES.getProperty(property);
	}

	public String getProperty(Enum<?> property) {
		return PROPERTIES.getProperty(property.name());
	}

	public boolean addProperty(Enum<?> key, String value) {
		return addProperty(key.name(), value);
	}

	public boolean addProperty(String key, String value) {
		reloadProperties();
		String val = PROPERTIES.getProperty(key);

		if (val == null || val.isEmpty())
			Logger.log.debug(LOGSTATE.CONFIG + "add new key: " + key + " --> value: " + value);
		else
			Logger.log.debug(LOGSTATE.CONFIG + "modified key: " + key + " --> value: " + value);

		PROPERTIES.setProperty(key, value);
		writeProperties();
		reloadProperties();
		return false;
	}

	private void addMissingProperties() {
		Properties current = (Properties) PROPERTIES.clone();
		Properties old = new Properties();
		try (InputStream in = new FileInputStream(PATH_CFG.toFile())) {
			old.load(in);
		} catch (IOException e) {
			Logger.log.error(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties " + e);
		}

		for (Entry<Object, Object> curr : current.entrySet()) {
			if (!old.containsKey(curr.getKey())) {
				Logger.log.debug(LOGSTATE.CONFIG + "add missing config entry: " + curr.getKey());
				old.setProperty(String.valueOf(curr.getKey()), String.valueOf(curr.getValue()));
			}
		}

		PROPERTIES = old;
		writeProperties();
	}

	private void writeProperties() {
		Logger.log.trace(LOGSTATE.CONFIG + "write to disk: " + PATH_CFG);
		try (OutputStream out = new FileOutputStream(PATH_CFG.toFile())) {
			PROPERTIES.store(out, "Storing Properties");
		} catch (IOException e) {
			Logger.log.error(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties " + e);
		}
	}

	private void reloadProperties() {
		Logger.log.trace(LOGSTATE.CONFIG + "reload from disk: " + PATH_CFG);
		try (InputStream in = new FileInputStream(PATH_CFG.toFile())) {
			PROPERTIES.load(in);
		} catch (IOException e) {
			Logger.log.error(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties " + e);
		}

	}

}
