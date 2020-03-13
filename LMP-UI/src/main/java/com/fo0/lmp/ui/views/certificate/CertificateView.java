package com.fo0.lmp.ui.views.certificate;

import java.io.IOException;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.WebsiteCertChecker;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.CertWebsite;
import com.fo0.lmp.ui.templates.AddWebsiteView;
import com.fo0.lmp.ui.templates.GridWebsites;
import com.fo0.lmp.ui.utils.UtilsSSLCertExpiry;
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
			UtilsWindow.createWindow("Add Website", new AddWebsiteView(CertWebsite.builder().build(), save -> {
				try {
					save.setDaysleft(UtilsSSLCertExpiry.check(save.getUrl()));
				} catch (IOException ex) {
				}
				grid.addWebsite(save);
			}), EWindowSize.Normal, true);
		}));

//		WindowOpenerButton btn = new WindowOpenerButton(
//				PopupConfiguration.builder().width(850).height(550).build().addParam("theme", ETheme.Dark.getTheme()),
//				new MultiHostConsole(grid.getList().stream().filter(x -> x.isActive()).collect(Collectors.toSet()),
//						""));

		MButton btnCheckAll = new MButton();
		btnCheckAll.withCaption("CHECK ALL");
		btnCheckAll.setIcon(MaterialIcons.CHECK);
		btnCheckAll.addClickListener(e -> {
			grid.getList().stream().filter(CertWebsite::isActive).forEach(i -> {
				try {
					i.setDaysleft(UtilsSSLCertExpiry.check(i.getUrl()));
				} catch (IOException ex) {
				}
			});
			grid.getDataProvider().refreshAll();
			WebsiteCertChecker.save(grid.getList());
		});
		layout.add(btnCheckAll);

		return layout;
	}

	private MButton createButton(String caption, Resource icon, ClickListener listener) {
		return new MButton(caption).withIcon(icon).withListener(listener);
	}

}
