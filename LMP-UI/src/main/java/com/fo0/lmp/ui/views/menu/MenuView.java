package com.fo0.lmp.ui.views.menu;

import org.vaadin.alump.materialicons.MaterialIcons;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.main.MainUI;
import com.fo0.lmp.ui.utils.ICON;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.fo0.lmp.ui.views.backup.BackupView;
import com.fo0.lmp.ui.views.certificate.CertificateView;
import com.fo0.lmp.ui.views.dashboard.DashboardView;
import com.fo0.lmp.ui.views.error.ErrorView;
import com.fo0.lmp.ui.views.manage.ActionListView;
import com.fo0.lmp.ui.views.manage.ManageHosts;
import com.fo0.lmp.ui.views.register.RegisterView;
import com.fo0.lmp.ui.views.settings.SettingsView;
import com.github.appreciated.app.layout.AppLayout;
import com.github.appreciated.app.layout.behaviour.AppLayoutComponent;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.design.AppLayoutDesign;
import com.github.appreciated.app.layout.builder.entities.DefaultNotification;
import com.github.appreciated.app.layout.builder.entities.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.MenuHeader;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
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
				.withDesign(AppLayoutDesign.MATERIAL)
				.add(new MenuHeader(MainUI.NAME, MainUI.VERSION, new ThemeResource("img/GP-Logo_q.png")))
				.withDefaultNavigationView(DashboardView.class).add("Home", ICON.DASHBOARD, DashboardView.class)
				.add("Hosts", ICON.HOST, ManageHosts.class)
				.add("Action", ICON.ACTION, ActionListView.class)
				.add("Certificate", ICON.CERTIFICATE, CertificateView.class)
				.add("Backup", ICON.BACKUP, BackupView.class)
				.add("Account", ICON.REGISTER, RegisterView.class).withNavigatorConsumer(e -> {
					e.setErrorView(ErrorView.class);
					// e.addView(CollectorDataView.class.getSimpleName(), CollectorDataView.class);
				}).add("Settings", ICON.SETTING, SettingsView.class).build();
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
