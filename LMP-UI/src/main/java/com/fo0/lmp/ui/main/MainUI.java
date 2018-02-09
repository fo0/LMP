package com.fo0.lmp.ui.main;

import javax.servlet.annotation.WebServlet;

import com.fo0.lmp.ui.config.CONSTANTS;
import com.fo0.lmp.ui.config.UIConfig;
import com.fo0.lmp.ui.controller.Controller;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("themebright")
@Push(PushMode.AUTOMATIC)
@Viewport("initial-scale=1, maximum-scale=1")
public class MainUI extends UI implements DetachListener {

	private static final long serialVersionUID = 4966998996112632022L;
	public static final String NAME = "Linux Management Platform";
	public static final String VERSION = "Version 1.2.2";
	private Logger logger = LoggerFactory.getLogger(MainUI.class);
	private static UIConfig config = null;

	@WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true, loadOnStartup = 1)
	@VaadinServletConfiguration(ui = MainUI.class, productionMode = true)
	public static class MainUIServlet extends VaadinServlet {

		private static final long serialVersionUID = -5364917552413492552L;

	}

	public void buildLayout() {
		UI.setCurrent(this);
		getPage().setTitle(NAME);
		new Controller(this);
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setErrorHandler(e -> {
			e.getThrowable().printStackTrace();
		});
		com.fo0.logger.CONSTANTS.LOGGER_LEVEL = "DEBUG";
		com.fo0.logger.CONSTANTS.LOGGER_ENABLE_COLOR = "true";
		CONSTANTS.bootstrap(new UIConfig("false"));
		config = CONSTANTS.getConfig();

		addDetachListener(this);
		logger.info("Application started.");
		logger.info("Accessing Page from Client: " + VaadinService.getCurrentRequest().getRemoteAddr());
		logger.info("Entering [" + UI.getCurrent() + "]: " + this);
		buildLayout();
		logger.info("register broadcaster");

		JavaScript.getCurrent().addFunction("aboutToClose", arguments -> detach());

		Page.getCurrent().getJavaScript().execute(
				"window.onbeforeunload = function (e) { var e = e || window.event; aboutToClose(); return; };");

	}

	@Override
	public void detach(DetachEvent event) {
		logger.info("## Detecting a Session/Window was closed. ##");
		logger.info("Starting Cleanups now ...");
		UI.getCurrent().close();
	}

	public static UIConfig getConfig() {
		return config;
	}

	public static void setConfig(UIConfig config) {
		MainUI.config = config;
		CONSTANTS.writeConfig(config);
	}

}
