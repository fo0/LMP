package com.fo0.lmp.ui.utils;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MWindow;

import com.fo0.lmp.ui.enums.EWindowSize;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public class UtilsWindow {

	public static MWindow createWindow(MWindow window, String caption, Component component, EWindowSize size,
			boolean attach) {
		MWindow w = createWindow(window, caption, component, size);
		if (attach)
			UI.getCurrent().addWindow(w);
		return w;
	}

	public static MWindow createWindow(String caption, Component component, EWindowSize size, boolean attach) {
		MWindow window = createWindow(caption, component, size);
		if (attach)
			UI.getCurrent().addWindow(window);
		return window;
	}

	public static MWindow createWindow(MWindow window, String caption, Component component, EWindowSize size) {
		switch (size) {
		case Small:
			return createWindow(window, caption, component, "640px", "480px");

		case Normal:
			return createWindow(window, caption, component, "800px", "600px");

		case Large:
			return createWindow(window, caption, component, "1280px", "768px");

		case Wide:
			return createWindow(window, caption, component, "950px", "600px");

		default:
			return createWindow(window, caption, component, "800px", "600px");
		}
	}

	public static MWindow createWindow(String caption, Component component, EWindowSize size) {
		switch (size) {
		case Small:
			return createWindow(caption, component, "640px", "480px");

		case Normal:
			return createWindow(caption, component, "800px", "600px");

		case Large:
			return createWindow(caption, component, "1280px", "768px");

		case Wide:
			return createWindow(caption, component, "950px", "600px");

		default:
			return createWindow(caption, component, "800px", "600px");
		}
	}

	public static MWindow createWindow(String caption, Component component, String width, String height) {
		return new MWindow(caption, component).withSize(MSize.size(width, height)).withCenter();
	}

	public static MWindow createWindow(MWindow window, String caption, Component component, String width,
			String height) {
		if (window == null) {
			return new MWindow(caption, component).withSize(MSize.size(width, height)).withCenter();
		} else {
			window.setCaption(caption);
			return window.withContent(component).withSize(MSize.size(width, height)).withCenter();
		}
	}

}
