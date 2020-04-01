package com.fo0.lmp.ui.views.login;

import com.fo0.fcf.logger.Logger;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.AccountManager;
import com.fo0.lmp.ui.main.MainUI;
import com.fo0.lmp.ui.model.Account;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.github.appreciated.material.MaterialTheme;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class LoginView extends AVerticalView {

	private static final long serialVersionUID = 4273653074117421617L;

	private Binder<Account> binder = new Binder<Account>(Account.class);

	private TextField username = new TextField("Username");
	private PasswordField password = new PasswordField("Password");
	private MHorizontalLayout buttons;

	public LoginView() {
		super();
		binder.setBean(new Account());
		initBuild();
	}

	@Override
	public void build() {
		addComponent(createLayout());
		alignAll(Alignment.MIDDLE_CENTER);

		if (username.isEmpty())
			username.focus();
		else
			password.focus();

		Utils.addKeyListener(action -> {
			Button btn = (Button) buttons.getComponent(0);
			btn.click();
		}, password, KeyCode.ENTER);
		
		try {
			runWhileAttached(this, () -> {
				if (MainUI.getConfig().isAutoLogin()) {
					username.setValue(AccountManager.load().getName());
					password.setValue(AccountManager.load().getPassword());
					MButton btn = ((MButton) buttons.getComponent(0));
					btn.click();
				}
			}, 0, 200, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MPanel createLayout() {
		MVerticalLayout root = new MVerticalLayout().withFullSize();
		password.setWidth("100%");
		username.setWidth("100%");
		root.addComponent(username);
		root.addComponent(password);

		String buttonCaption = "Login";

		buttons = UtilsComponents.Button_Apply_Discard_Layout(buttonCaption, "Discard", login -> {
			if (binder.getBean().equals(AccountManager.load())) {
				Logger.log.info("Login successful");
				setSessionUser(binder.getBean());
				getController().init();
				getController().getMenu().addNotificationAndTray("Login", "Login successful");
			} else {
				UtilsNotification.notificationTray("Login failed", "");
			}
		}, discard -> {
			binder.setBean(new Account());
		});

		root.addComponent(buttons);
		return new MPanel(root).withStyleName(MaterialTheme.CARD_2).withCaption("Login")
				.withSize(MSize.size("400px", "350px"));
	}

	@Override
	public void init() {
		setSizeFull();
		binder.forField(username).bind(Account::getName, Account::setName);
		binder.forField(password).bind(Account::getPassword, Account::setPassword);
	}

}
