package com.fo0.lmp.ui.views.certificate;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.WebsiteCertChecker;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Website;
import com.fo0.lmp.ui.templates.AddWebsiteView;
import com.fo0.lmp.ui.templates.GridWebsites;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

public class CertificateView extends AVerticalView {

	private static final long serialVersionUID = 8205214509304261361L;

	private GridWebsites grid = null;

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

		grid = new GridWebsites(WebsiteCertChecker.load());
		grid.withFullSize();
	}

	private MHorizontalLayout createButtonLayout() {
		MHorizontalLayout layout = new MHorizontalLayout();
		layout.add(createButton("Website", MaterialIcons.ADD, e -> {
			UtilsWindow.createWindow("Add Website", new AddWebsiteView(Website.builder().build(), save -> {
				grid.addWebsite(save);
			}), EWindowSize.Normal, true);
		}));

//		WindowOpenerButton btn = new WindowOpenerButton(
//				PopupConfiguration.builder().width(850).height(550).build().addParam("theme", ETheme.Dark.getTheme()),
//				new MultiHostConsole(grid.getList().stream().filter(x -> x.isActive()).collect(Collectors.toSet()),
//						""));

		MButton btn = new MButton();

		// btn.click();
		btn.withCaption("CHECK ALL");
		btn.setIcon(MaterialIcons.CHECK);
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
