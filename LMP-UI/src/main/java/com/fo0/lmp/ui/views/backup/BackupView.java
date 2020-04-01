package com.fo0.lmp.ui.views.backup;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.ActionLoader;
import com.fo0.lmp.ui.data.HostLoader;
import com.fo0.lmp.ui.data.HostPropertyLoader;
import com.fo0.lmp.ui.data.KeyLoader;
import com.fo0.lmp.ui.data.WebsiteCertificateLoader;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.model.CertWebsite;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.HostProperty;
import com.fo0.lmp.ui.model.Key;
import com.fo0.lmp.ui.templates.BackupExportConfigView;
import com.fo0.lmp.ui.templates.BackupImportConfigView;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.github.appreciated.material.MaterialTheme;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

public class BackupView extends AVerticalView {

	private static final long serialVersionUID = -3006144325447149355L;

	@Override
	public void build() {
		addComponent(createImportAndExportLayout("Actions", "actions.json.cfg", Action.class, () -> ActionLoader.load(),
				resultSet -> ActionLoader.save(resultSet)));

		addComponent(createImportAndExportLayout("Hosts", "hosts.json.cfg", Host.class, () -> HostLoader.load(),
				resultSet -> HostLoader.save(resultSet)));

		addComponent(createImportAndExportLayout("Host-Properties", "host-properties.json.cfg", HostProperty.class,
				() -> HostPropertyLoader.load(), resultSet -> HostPropertyLoader.save(resultSet)));

		addComponent(createImportAndExportLayout("Website-Certificates", "website-certificates.json.cfg", CertWebsite.class,
						() -> WebsiteCertificateLoader.load(), resultSet -> WebsiteCertificateLoader.save(resultSet)));

		addComponent(createImportAndExportLayout("Keys", "keys.json.cfg", Key.class, () -> KeyLoader.load(),
				resultSet -> KeyLoader.save(resultSet)));
	}

	public <T> MPanel createImportAndExportLayout(String caption, String filename, Class<T> type,
			Supplier<Set<T>> loader, Consumer<Set<T>> save) {
		MHorizontalLayout layout = new MHorizontalLayout().withMargin(true).withSpacing(true);
		layout.add(createButton("Export", MaterialIcons.CLOUD_UPLOAD, e -> {
			String s = "";
			Set<T> actions = loader.get();
			if (actions != null && !actions.isEmpty()) {
				s = new GsonBuilder().setPrettyPrinting().create().toJson(actions);
			}

			UtilsWindow.createWindow(String.format("Export %s Config", caption),
					new BackupExportConfigView(s, filename), EWindowSize.Normal, true);
		}));

		layout.add(createButton("Import", MaterialIcons.CLOUD_DOWNLOAD, e -> {
			UtilsWindow.createWindow(String.format("Import Cert-Website Config", caption),
					new BackupImportConfigView((config, override) -> {
						List<T> hosts = com.fo0.fcf.utils.parser.Parser.JSON.parseList(config, type);
						if (!override) {
							Set<T> existingHosts = loader.get();
							if (existingHosts != null && !existingHosts.isEmpty())
								hosts.addAll(existingHosts);
						}

						save.accept(Sets.newHashSet(hosts));
						UtilsNotification.saved(String.format("Imported %s Config", caption));
					}), EWindowSize.Normal, true);
		}));
		return new MPanel(layout).withCaption(caption).withStyleName(MaterialTheme.CARD_1);
	}

	@Override
	public void init() {

	}

	private MButton createButton(String caption, Resource icon, ClickListener listener) {
		return new MButton(caption).withIcon(icon).withListener(listener);
	}

}
