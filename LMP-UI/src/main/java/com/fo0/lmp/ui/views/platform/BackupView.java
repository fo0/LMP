package com.fo0.lmp.ui.views.platform;

import java.util.Set;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.ActionManager;
import com.fo0.lmp.ui.data.LinuxHostManager;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.templates.BackupExportConfigView;
import com.fo0.lmp.ui.templates.BackupImportConfigView;
import com.fo0.lmp.ui.utils.Parser;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.github.appreciated.material.MaterialTheme;
import com.google.gson.GsonBuilder;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

public class BackupView extends AVerticalView {

	private static final long serialVersionUID = -3006144325447149355L;

	@Override
	public void build() {
		addComponent(createActionConfigayout());
		addComponent(createHostConfigLayout());
	}

	public MPanel createActionConfigayout() {
		MHorizontalLayout layout = new MHorizontalLayout().withMargin(true).withSpacing(true);
		layout.add(createButton("Export", MaterialIcons.BACKUP, e -> {
			String s = "";
			Set<Action> actions = ActionManager.load();
			if (actions != null && !actions.isEmpty()) {
				s = new GsonBuilder().setPrettyPrinting().create().toJson(actions);
			}
			UtilsWindow.createWindow("Export Action Config", new BackupExportConfigView(s, "actions.json.cfg"),
					EWindowSize.Normal, true);
		}));

		layout.add(createButton("Import", MaterialIcons.IMPORTANT_DEVICES, e -> {
			UtilsWindow.createWindow("Import Action Config", new BackupImportConfigView((config, override) -> {
				Set<Action> hosts = Parser.parseSet(config, Action.class);
				if (!override) {
					Set<Action> existingHosts = ActionManager.load();
					if (existingHosts != null && !existingHosts.isEmpty())
						hosts.addAll(existingHosts);
				}
				ActionManager.save(hosts);
				UtilsNotification.saved("Imported Host Config");
			}), EWindowSize.Normal, true);
		}));
		return new MPanel(layout).withCaption("Action").withStyleName(MaterialTheme.CARD_1);
	}

	public MPanel createHostConfigLayout() {
		MHorizontalLayout layout = new MHorizontalLayout().withMargin(true).withSpacing(true);
		layout.add(createButton("Export", MaterialIcons.BACKUP, e -> {
			String s = "";
			Set<Host> hosts = LinuxHostManager.load();
			if (hosts != null && !hosts.isEmpty()) {
				s = new GsonBuilder().setPrettyPrinting().create().toJson(hosts);
			}
			UtilsWindow.createWindow("Export Host Config", new BackupExportConfigView(s, "hosts.json.cfg"),
					EWindowSize.Normal, true);
		}));

		layout.add(createButton("Import", MaterialIcons.IMPORTANT_DEVICES, e -> {
			UtilsWindow.createWindow("Import Host Config", new BackupImportConfigView((config, override) -> {
				Set<Host> hosts = Parser.parseSet(config, Host.class);
				if (!override) {
					Set<Host> existingHosts = LinuxHostManager.load();
					if (existingHosts != null && !existingHosts.isEmpty())
						hosts.addAll(existingHosts);
				}
				LinuxHostManager.save(hosts);
				UtilsNotification.saved("Imported Host Config");
			}), EWindowSize.Normal, true);
		}));
		return new MPanel(layout).withCaption("Host").withStyleName(MaterialTheme.CARD_1);
	}

	@Override
	public void init() {

	}

	private MButton createButton(String caption, Resource icon, ClickListener listener) {
		return new MButton(caption).withIcon(icon).withListener(listener);
	}

}
