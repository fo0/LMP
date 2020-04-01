package com.fo0.lmp.ui.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.fo0.fcf.logger.LOGSTATE;
import com.fo0.fcf.logger.Logger;

import org.apache.commons.lang3.StringUtils;

import com.fo0.lmp.ui.model.Host;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class HostLoader {

	private static String path = System.getProperty("jboss.server.config.dir") + "/hosts.json";

	public static void deleteById(String id) {
		Set<Host> properties = load();
		properties.removeIf(e -> StringUtils.equals(e.getId(), id));
		save(properties);
	}

	public static void delete(Host host) {
		Set<Host> properties = load();
		properties.remove(host);
		save(properties);
	}

	public static Host getHostById(String id) {
		return load().stream().filter(e -> e.getId().contentEquals(id)).findFirst().orElse(null);
	}

	public static Set<Host> load() {
		createConfig();
		try {
			Set<Host> acc = parse(new File(path), Host.class);

			if (acc == null)
				return new HashSet<>();
			else
				return acc;

		} catch (Exception e) {
			Logger.log.error(LOGSTATE.FAILED + "to load SSHHost from file: " + path);
			e.printStackTrace();
		}
		return null;
	}

	public static void save(Set<Host> SSHHost) {
		createConfig();
		try {
			write(SSHHost, path);
		} catch (Exception e) {
			Logger.log.error(LOGSTATE.FAILED + "to load SSHHost from file: " + path, e);
		}
	}

	private static void createConfig() {
		if (!Paths.get(path).toFile().exists()) {
			try {
				Paths.get(path).toFile().createNewFile();
			} catch (Exception e) {
				Logger.log.info(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties ", e);
			}
		}
	}

	private static <T> Set<T> parse(File file, Class<T> _Class) {
		if (file == null || !file.exists())
			return null;

		FileInputStream fis = null;

		try {
			Gson gson = new Gson();
			fis = new FileInputStream(file);
			Set<T> obj = gson.fromJson(new InputStreamReader(fis, "UTF-8"),
					TypeToken.getParameterized(HashSet.class, _Class).getType());

			if (obj == null)
				return null;

			return obj;
		} catch (Exception e) {
			Logger.log.debug(LOGSTATE.FAILED + "to read json file path: " + file.getPath(), e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				Logger.log.debug(LOGSTATE.FAILED + "close FileInputStream " + e);
			}
		}
		return null;
	}

	private static <T> void write(Set<T> obj, String destFile) {
		createConfig();
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8")) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(obj, writer);
			writer.close();
		} catch (IOException e) {
			Logger.log.debug(LOGSTATE.FAILED + "to read json Object " + obj.getClass(), e);
		}
	}

}
