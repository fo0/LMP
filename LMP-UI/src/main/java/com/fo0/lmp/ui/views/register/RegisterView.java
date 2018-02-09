package com.fo0.lmp.ui.views.register;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.AccountManager;
import com.fo0.lmp.ui.model.Account;
import com.fo0.lmp.ui.utils.ETheme;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.github.appreciated.material.MaterialTheme;
import com.vaadin.data.Binder;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class RegisterView extends AVerticalView {

	private static final long serialVersionUID = 4273653074117421617L;

	private Binder<Account> binder = new Binder<Account>(Account.class);

	private TextField username = new TextField("Username");
	private PasswordField password = new PasswordField("Password");
	private ComboBox<ETheme> theme = new ComboBox<ETheme>("Theme");

	public RegisterView() {
		super();
		if (AccountManager.load() == null) {
			binder.setBean(new Account());
		} else {
			binder.setBean(AccountManager.load());
		}

		initBuild();
	}

	@Override
	public void build() {
		addComponent(createLayout());
		alignAll(Alignment.MIDDLE_CENTER);
		username.focus();
	}

	private MPanel createLayout() {
		MVerticalLayout root = new MVerticalLayout().withFullSize();
		password.setWidth("100%");
		username.setWidth("100%");
		theme.setWidth("100%");
		root.addComponent(username);
		root.addComponent(password);
		root.addComponent(theme);
		root.addComponent(UtilsComponents.Button_Apply_Discard_Layout("Register", "Discard", login -> {
			AccountManager.save(binder.getBean());
			setSessionUser(binder.getBean());
			getController().init();
		}, discard -> {
			binder.setBean(new Account());
		}));

		return new MPanel(root).withStyleName(MaterialTheme.CARD_2).withCaption("Register")
				.withSize(MSize.size("400px", "450px"));
	}

	@Override
	public void init() {
		setSizeFull();
		binder.forField(username).bind(Account::getName, Account::setName);
		binder.forField(password).bind(Account::getPassword, Account::setPassword);
		theme.setItems(ETheme.values());
		binder.forField(theme).bind(Account::getTheme, Account::setTheme);
	}

}
