package com.fo0.lmp.ui.controller;

import com.fo0.fcf.logger.LOGSTATE;
import com.fo0.fcf.logger.Logger;

import com.fo0.lmp.ui.data.AccountManager;
import com.fo0.lmp.ui.main.MainUI;
import com.fo0.lmp.ui.model.Account;
import com.fo0.lmp.ui.utils.ETheme;
import com.fo0.lmp.ui.views.login.LoginView;
import com.fo0.lmp.ui.views.menu.MenuView;
import com.fo0.lmp.ui.views.register.RegisterView;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class Controller {
	private MainUI main;
	private MenuView menu = null;

	public Controller(MainUI main) {
		this.main = main;

		// init Controller Session Variable
		VaadinSession.getCurrent().setAttribute(Controller.class, this);

		init();

		Logger.log.info("CM initialized successful");

	}

	public void init() {
		if (VaadinSession.getCurrent().getAttribute(Account.class) == null) {
			Logger.log.info(LOGSTATE.GENERAL + "no session found");
			if (AccountManager.load() == null) {
				Logger.log.info(LOGSTATE.GENERAL + "no account found, creating register view");
				main.setContent(new RegisterView());
			} else {
				Logger.log.info(LOGSTATE.GENERAL + "account found, navigate to login view");
				main.setContent(new LoginView());
			}
		} else {
			initApps();
			Logger.log.info("Found existing cookie, re-init access");
			Account acc = AccountManager.load();
			if (acc.getTheme() == null) {
				acc.setTheme(ETheme.Bright);
				AccountManager.save(acc);
			}
			setTheme(acc.getTheme());
			String tmpState = new String(UI.getCurrent().getNavigator().getState());
			UI.getCurrent().getNavigator().navigateTo(tmpState);
		}
	}

	public MainUI getMainApplication() {
		return main;
	}

	public MenuView getMenu() {
		return menu;
	}

	private void initApps() {
		initMenu();
		main.setContent(menu);
	}

	public void initMenu() {
		menu = new MenuView(main);
	}

	public boolean initREST() {
		return false;
	}

	public void setTheme(ETheme theme) {
		main.setTheme(theme.getTheme());
	}

	public ETheme getTheme() {
		try {
			return ETheme.valueOf(main.getTheme());
		} catch (Exception e) {
			Logger.log.error("failed to load theme: " + main.getTheme());
		}
		return ETheme.Bright;
	}

}