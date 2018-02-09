package com.fo0.lmp.ui.views.menu;

import org.vaadin.alump.materialicons.MaterialIcons;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.main.MainUI;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.fo0.lmp.ui.views.dashboard.DashboardView;
import com.fo0.lmp.ui.views.error.ErrorView;
import com.fo0.lmp.ui.views.manage.ActionListView;
import com.fo0.lmp.ui.views.manage.ManageHosts;
import com.fo0.lmp.ui.views.platform.BackupView;
import com.fo0.lmp.ui.views.register.RegisterView;
import com.fo0.lmp.ui.views.settings.SettingsView;
import com.github.appreciated.app.layout.behaviour.AppLayoutComponent;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayout;
import com.github.appreciated.app.layout.builder.design.AppBarDesign;
import com.github.appreciated.app.layout.builder.entities.DefaultNotification;
import com.github.appreciated.app.layout.builder.entities.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.MenuHeader;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.UI;

public class MenuView extends AVerticalView {

	private static final long serialVersionUID = -8600863224276134722L;

	private AppLayoutComponent menu = null;
	private MainUI main = null;

	private DefaultNotificationHolder notificationBar;

	public MenuView(MainUI main) {
		this.main = main;
		initBuild();
	}

	@Override
	public void build() {
		addComponent(menu);
	}

	@Override
	public void init() {
		setSizeFull();
		setMargin(false);
		notificationBar = new DefaultNotificationHolder();

		// workaround

		menu = AppLayout.getDefaultBuilder(Behaviour.LEFT_HYBRID).withTitle(MainUI.NAME)
				.withDesign(AppBarDesign.MATERIAL)
				.add(new MenuHeader(MainUI.NAME, MainUI.VERSION, new ThemeResource("img/GP-Logo_q.png")))
				.withDefaultNavigationView(DashboardView.class).add("Home", VaadinIcons.HOME, DashboardView.class)
				.add("Hosts", VaadinIcons.GLOBE, ManageHosts.class)
				.add("Action", MaterialIcons.LIST, ActionListView.class)
				.add("Backup", MaterialIcons.SETTINGS, BackupView.class)
				.add("Account", VaadinIcons.USER, RegisterView.class).withNavigatorConsumer(e -> {
					e.setErrorView(ErrorView.class);
					// e.addView(CollectorDataView.class.getSimpleName(), CollectorDataView.class);
				}).add("Settings", VaadinIcons.COG, SettingsView.class).build();
	}

	public void addNotificationAndTray(String caption, String description) {
		UI.getCurrent().access(() -> {
			notificationBar.addNotification(new DefaultNotification(caption, description));
			UtilsNotification.notificationTray(caption, description);
		});
	}

	public void addNotification(String caption, String description) {
		UI.getCurrent().access(() -> {
			notificationBar.addNotification(new DefaultNotification(caption, description));
		});
	}

}
