package com.fo0.lmp.ui.views.settings;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.config.UIConfig;
import com.fo0.lmp.ui.main.MainUI;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.github.appreciated.material.MaterialTheme;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Binder;
import com.vaadin.ui.CheckBox;

public class SettingsView extends AVerticalView {

	private static final long serialVersionUID = 4573631592171888672L;

	private ResponsiveLayout layout = new ResponsiveLayout().withDefaultRules(12, 12, 6, 6).withSpacing();

	private Binder<UIConfig> binder = new Binder<UIConfig>(UIConfig.class);

	private CheckBox autoLogin = new CheckBox("Auto Login");

	public SettingsView() {
		super();
		binder.setBean(MainUI.getConfig());
		initBuild();
	}

	@Override
	public void build() {
		ResponsiveRow layoutButtons = layout.addRow().withDefaultRules(12, 12, 6, 6).withStyleName(MaterialTheme.CARD_1,
				true);
		ResponsiveRow layoutOptions = layout.addRow().withDefaultRules(12, 12, 6, 6).withStyleName(MaterialTheme.CARD_1,
				true);

		layoutOptions.withComponents(new MHorizontalLayout(autoLogin));

		layoutButtons.withComponents(new MHorizontalLayout(
				new MButton(MaterialIcons.SAVE).withStyleName(MaterialTheme.BUTTON_FRIENDLY).withListener(e -> {
					try {
						MainUI.setConfig(binder.getBean());
						getController().getMenu().addNotificationAndTray("Changed Settings", "");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}), new MButton(MaterialIcons.DELETE).withStyleName(MaterialTheme.BUTTON_DANGER).withListener(e -> {
					UtilsNotification.notificationTray("Discarded Settings", "");
				})));

		setMargin(true);
		addComponent(layout);
	}

	@Override
	public void init() {
		binder.forField(autoLogin).bind(UIConfig::isAutoLogin, UIConfig::setAutoLogin);
	}

}