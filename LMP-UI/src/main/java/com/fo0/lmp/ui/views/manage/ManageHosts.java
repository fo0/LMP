package com.fo0.lmp.ui.views.manage;

import java.util.stream.Collectors;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.LinuxHostManager;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.templates.AddHostView;
import com.fo0.lmp.ui.templates.GridHosts;
import com.fo0.lmp.ui.templates.MultiHostConsole;
import com.fo0.lmp.ui.utils.ETheme;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.fo0.vaadin.browserwindowopener.main.PopupConfiguration;
import com.fo0.vaadin.browserwindowopener.main.WindowOpenerButton;
import com.github.appreciated.material.MaterialTheme;
import com.vaadin.server.BrowserWindowOpener;
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
		// TODO: Tabs for every

		grid = new GridHosts(LinuxHostManager.load());
		grid.withFullSize();
	}

	private MHorizontalLayout createButtonLayout() {
		MHorizontalLayout layout = new MHorizontalLayout();
		layout.add(createButton("Host", MaterialIcons.ADD, e -> {
			UtilsWindow.createWindow("Add Host", new AddHostView(Host.builder().build(), save -> {
				grid.addHost(save);
			}), EWindowSize.Normal, true);
		}));

		WindowOpenerButton btn = new WindowOpenerButton(
				PopupConfiguration.builder().width(850).height(550).build().addParam("theme", ETheme.Dark.getTheme()),
				new MultiHostConsole(grid.getList().stream().filter(x -> x.isActive()).collect(Collectors.toSet()),
						""));
		// btn.click();
		btn.withCaption("Multi-Console");
		btn.setIcon(MaterialIcons.OPEN_IN_NEW);
		layout.add(btn);
		// layout.add(createButton("MultiHost-Console", MaterialIcons.ADD, e -> {
		// UtilsWindow.createWindow("Multi-Console",
		// ,
		// EWindowSize.Wide, true);
		// }));
		return layout;
	}

	private MButton createButton(String caption, Resource icon, ClickListener listener) {
		return new MButton(caption).withIcon(icon).withListener(listener);
	}

}