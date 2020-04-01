package com.fo0.lmp.ui.utils;

import com.fo0.fcf.logger.LOGSTATE;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class UtilsNotification {

	private static final String WARNING = "Warning";
	private static final String ERROR = "Error";
	private static final String INFO = "Info";

	private static final String NOT_IMPLEMENTED = LOGSTATE.NOT_IMPLEMENTED.name();
	private static final String NOT_SUPPORTED = LOGSTATE.NOT_SUPPORTED.name();

	public static Notification discard() {
		return notification(INFO, "Discarded", Type.TRAY_NOTIFICATION);
	}

	public static Notification saved() {
		return saved("Saved");
	}

	public static Notification saved(String text) {
		return notificationTray(INFO, text);
	}

	public static Notification error(String message) {
		return notification(ERROR, message, Type.ERROR_MESSAGE);
	}

	public static Notification error() {
		return notification(ERROR, LOGSTATE.FAILED.name(), Type.ERROR_MESSAGE);
	}

	public static Notification notSupported() {
		return notSupported(null);
	}

	public static Notification notSupported(String message) {
		return notificationTray(NOT_SUPPORTED, message);
	}

	public static Notification notImplemented() {
		return notImplemented(null);
	}

	public static Notification notImplemented(String message) {
		return notificationTray(NOT_IMPLEMENTED, message);
	}

	public static Notification notificationTray(String text, String description) {
		return notification(null, text, description, Type.TRAY_NOTIFICATION,
				ValoTheme.NOTIFICATION_TRAY + " " + STYLES.NOTIFICATION_CAPTION_BLUE);
	}

	public static Notification notification(String text, String description, Type type) {
		return notification(null, text, description, type, null, null, 0);
	}

	public static Notification notification(String text, String description, Type type, String style) {
		return notification(null, text, description, type, null, style, 0);
	}

	public static Notification notification(String imagePath, String text, String description, Type type,
			String style) {
		return notification(imagePath, text, description, type, null, style, 0);
	}

	public static Notification notification(String text, String description, Type type, Position pos, String style) {
		return notification(null, text, description, type, pos, style, 0);
	}

	public static Notification notification(String pathIcon, String caption, String description, Type type,
			Position pos, String style, long duration) {
		Notification not = new Notification("<b>" + caption + "</b>", type);

		if (description != null && !description.isEmpty())
			not.setDescription(description);

		if (pathIcon != null && !pathIcon.isEmpty())
			not.setIcon(new ThemeResource(pathIcon));

		not.setHtmlContentAllowed(true);

		if (duration != 0)
			not.setDelayMsec(Integer.parseInt(String.valueOf(duration)));

		if (pos != null)
			not.setPosition(pos);

		if (style != null && !style.isEmpty())
			not.setStyleName(style);

		not.show(Page.getCurrent());
		return not;
	}

}