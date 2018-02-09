package com.fo0.lmp.ui.utils;

import java.util.Base64;

import com.fo0.logger.LOGSTATE;
import com.fo0.logger.Logger;
import com.google.gson.Gson;
import com.vaadin.ui.UI;

public class UtilsNavigator {

	public static void navigateTo(Class<?> clazz) {
		Logger.log.info(LOGSTATE.GENERAL + "navigate to: " + clazz.getName());
		UI.getCurrent().getNavigator().navigateTo(clazz.getSimpleName());
	}

	public static void navigateTo(Class<?> clazz, Object param) {
		Logger.log.info(LOGSTATE.GENERAL + "navigate to: " + clazz.getName() + " | Param: " + param);
		UI.getCurrent().getNavigator().navigateTo(
				clazz.getSimpleName() + "/" + Base64.getEncoder().encodeToString(new Gson().toJson(param).getBytes()));
	}

}
