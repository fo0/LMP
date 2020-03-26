package com.fo0.lmp.ui.views.host;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.LinuxHostManager;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.templates.AddHostView;
import com.fo0.lmp.ui.templates.GridHosts;
import com.fo0.lmp.ui.templates.MultiHostConsole;
import com.fo0.lmp.ui.utils.ETheme;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsHosts;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.fo0.vaadin.browserwindowopener.main.PopupConfiguration;
import com.fo0.vaadin.browserwindowopener.main.WindowOpenerButton;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

public class ManageHosts extends AVerticalView {

	private static final long serialVersionUID = 8717466836412861171L;

	private GridHosts grid = null;

	@Override
	public void build() {
		setSizeFull();
		addComponent(createButtonLayout());
		addComponent(grid);
		expand(grid);
	}

	@Override
	public void init() {
		Set<Host> hosts = LinuxHostManager.load();
		grid = new GridHosts(hosts);
		grid.withFullSize();

		runWhileAttached(grid, () -> {
			if (CollectionUtils.isEmpty(hosts)) {
				return;
			}

			hosts.parallelStream().forEach(e -> {
				// refresh status
				e.setReachable(Utils.isAddressReachable(e.getAddress(), e.getPort(), 500));
				grid.getDataProvider().refreshItem(e);
			});

			// save current state
			LinuxHostManager.save(hosts);
		}, -1, 100);
	}

	private MHorizontalLayout createButtonLayout() {
		MHorizontalLayout layout = new MHorizontalLayout();
		layout.add(createButton("Host", MaterialIcons.ADD, e -> {
			UtilsWindow.createWindow("Add Host", new AddHostView(Host.builder().build(), save -> {
				// Retrieve Host Informations like hostname, os
				save = UtilsHosts.getHostInformation(save);
				grid.addHost(save);
			}), "782px", "700px", true);
		}));

		WindowOpenerButton btn = new WindowOpenerButton(
				PopupConfiguration.builder().width(850).height(550).build().addParam("theme", ETheme.Dark.getTheme()),
				() -> {
					return new MultiHostConsole(grid.getList().stream().filter(x -> x.isActive())
							.filter(x -> x.isReachable()).collect(Collectors.toSet()), "");
				});

		// btn.click();
		btn.withCaption("Multi-Console");
		btn.setIcon(MaterialIcons.OPEN_IN_NEW);
		layout.add(btn);
		return layout;
	}

	private MButton createButton(String caption, Resource icon, ClickListener listener) {
		return new MButton(caption).withIcon(icon).withListener(listener);
	}

}