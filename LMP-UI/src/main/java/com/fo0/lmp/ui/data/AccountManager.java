package com.fo0.lmp.ui.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;

import com.fo0.lmp.ui.model.Account;
import com.fo0.logger.LOGSTATE;
import com.fo0.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AccountManager {

	private static String path = System.getProperty("jboss.server.config.dir") + "/LMP_Account.json";

	public static Account load() {
		createConfig();
		try {
			Account acc = parse(new File(path), Account.class);

			if (acc == null)
				return null;
			else
				return acc;

		} catch (Exception e) {
			Logger.log.error(LOGSTATE.FAILED + "to load account from file: " + path);
			e.printStackTrace();
		}
		return null;
	}

	public static void save(Account account) {
		createConfig();
		try {
			write(account, path);
		} catch (Exception e) {
			Logger.log.error(LOGSTATE.FAILED + "to load account from file: " + path);
			e.printStackTrace();
		}
	}

	private static void createConfig() {
		if (!Paths.get(path).toFile().exists()) {
			try {
				Paths.get(path).toFile().createNewFile();
			} catch (Exception e) {
				Logger.log.info(LOGSTATE.FAILED + LOGSTATE.CONFIG.toString() + "to load Properties " + e);
				e.printStackTrace();
			}
		}
	}

	private static <T> T parse(File file, Class<T> _Class) {

		if (file == null || !file.exists())
			return null;

		FileInputStream fis = null;

		try {
			Gson gson = new Gson();
			fis = new FileInputStream(file);
			T obj = gson.fromJson(new InputStreamReader(fis, "UTF-8"), _Class);

			if (obj == null)
				return null;

			return obj;
		} catch (Exception e) {
			Logger.log.debug(LOGSTATE.FAILED + "to read json file path: " + file.getPath() + " " + e);
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.log.debug(LOGSTATE.FAILED + "close FileInputStream " + e);
			}
		}
		return null;
	}

	private static <T> void write(T obj, String destFile) {
		createConfig();
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8")) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(obj, writer);
			writer.close();
		} catch (IOException e) {
			Logger.log.debug(LOGSTATE.FAILED + "to read json Object " + obj.getClass() + " " + e);
			e.printStackTrace();
		}
	}

}
