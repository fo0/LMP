package com.fo0.lmp.ui.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fo0.fcf.logger.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Related to this post:
 * https://stackoverflow.com/questions/20773850/gson-typetoken-with-dynamic-arraylist-item-type
 *
 * @author max
 *
 */

public class Parser {

	public static Map<String, String> parseFromString(String json, String... keys) {
		HashMap<String, String> map = new HashMap<String, String>();
		JsonObject p = new JsonParser().parse(json).getAsJsonObject();

		if (p == null) {
			return null;
		}

		for (String v : keys) {
			String entry = "";
			JsonElement ele = p.get(v);
			if (ele != null) {
				entry = ele.getAsString();
			}

			map.put(v, entry);
		}

		return map;
	}

	public static <T> T parse(String json, Type type) {
		if (json == null || json.isEmpty())
			return null;

		try {
			T array = new Gson().fromJson(json, type);

			if (array == null)
				return null;

			return array;
		} catch (Exception e) {
			Logger.log.info("parse JSON: " + json + " " + e);
			Logger.log.debug("parse JSON: " + json + " " + e, e);
		}

		return null;
	}

	public static <T> List<T> parseList(String json, Class<T> _Class) {
		if (json == null || json.isEmpty())
			return null;

		return parse(json, TypeToken.getParameterized(ArrayList.class, _Class).getType());
	}

	public static <T> Set<T> parseSet(String json, Class<T> _Class) {
		if (json == null || json.isEmpty())
			return null;

		return parse(json, TypeToken.getParameterized(HashSet.class, _Class).getType());
	}

	public static <T> Map<String, T> parseMap(String json, Class... clazz) {
		return parse(json, TypeToken.getParameterized(Map.class, clazz).getType());
	}

	public static <T> T parse(String json, Class<T> _Class) {
		return parse(json, TypeToken.getParameterized(_Class).getType());
	}

}