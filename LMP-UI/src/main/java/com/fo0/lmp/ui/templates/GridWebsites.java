package com.fo0.lmp.ui.templates;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.data.WebsiteCertChecker;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.model.CertWebsite;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.utils.ICON;
import com.fo0.lmp.ui.utils.STYLES;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsSSLCertExpiry;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.renderers.HtmlRenderer;

public class GridWebsites extends MGrid<CertWebsite> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -949632134596892202L;
	private Set<CertWebsite> list = new HashSet<CertWebsite>();

	public GridWebsites(Set<CertWebsite> websites) {
		super(CertWebsite.class);

		if (list != null)
			this.list = websites;

		build();
	}

	private void build() {
		try {
			setItems(list);
		} catch (Exception e2) {
			list = new HashSet<CertWebsite>();
			setItems(list);
		}

		addColumn(e -> "Linux").setId("os").setCaption("Platform");

		addColumn(e -> {
			if (e.isActive()) {
				return ICON.CHECK.getHtml();
			} else {
				return ICON.CANCEL.getHtml();
			}
		}, new HtmlRenderer()).setId("activecheck").setCaption("Active").setStyleGenerator(e -> {
			if (e.isActive()) {
				return STYLES.ICON_GREEN;
			} else {
				return STYLES.ICON_RED;
			}
		});

		addColumn(e -> {
			if (e.isStatus()) {
				return ICON.CHECK.getHtml();
			} else {
				return ICON.CANCEL.getHtml();
			}
		}, new HtmlRenderer()).setId("statuscheck").setCaption("Status").setStyleGenerator(e -> {
			if (e.isStatus()) {
				return STYLES.ICON_GREEN;
			} else {
				return STYLES.ICON_RED;
			}
		});

		addComponentColumn(e -> {
			MHorizontalLayout labels = new MHorizontalLayout();
			if (e.getHost() != null) {
				labels.add(new MLabel().withValue(ICON.HOST.getHtml()).withContentMode(ContentMode.HTML));
			}

			if (e.getAction() != null) {
				labels.add(new MLabel().withValue(ICON.ACTION.getHtml()).withContentMode(ContentMode.HTML));
			}

			return labels;
		}).setId("info").setCaption("Info");

		addComponentColumn(e -> {
			return addActionButton(e);
		}).setId("menu").setCaption("Action");

		// setColumns("label", "os", "address", "port", "activecheck", "action");
		setColumns("url", "port", "activecheck", "statuscheck", "daysleft", "info", "menu");
		sort("url", SortDirection.ASCENDING);
	}

	public void setList(Set<CertWebsite> list) {
		this.list = list;
	}

	public Set<CertWebsite> getList() {
		return list;
	}

	public void addWebsite(CertWebsite website) {
		list.remove(website);
		list.add(website);
		getDataProvider().refreshAll();
		WebsiteCertChecker.save(list);
	}

	public void addWebsite(CertWebsite website, boolean save) {
		list.remove(website);
		list.add(website);
		getDataProvider().refreshAll();
		if (save)
			WebsiteCertChecker.save(list);
	}

	public void removeWebsite(CertWebsite website) {
		list.remove(website);
		getDataProvider().refreshAll();
		WebsiteCertChecker.save(list);
	}

	private void execute(Host host, Action action) {
		UtilsWindow.createWindow("Multi-Console",
				new MultiHostConsole(Stream.of(host).collect(Collectors.toSet()), true, action.getCommand()),
				EWindowSize.Wide, true);
	}

	private MenuBar addActionButton(CertWebsite website) {
		return UtilsComponents.multiActionButton(cmd -> {

			switch (cmd.getText()) {
			case "Delete":
				new ConfirmDialog("Delete Website: " + website.getLabel(), ok -> {
					removeWebsite(website);
				}, discard -> {
					removeWebsite(website);
				});

				break;

			case "Check":
				try {
					website.setDaysleft(UtilsSSLCertExpiry.check(website.getUrl()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				addWebsite(website);
				break;

			case "Ping":
				execute(website, ELinuxActions.PING);
				break;

			case "Update":
				execute(website.getHost(), website.getAction());
				break;

			case "Edit":
				UtilsWindow.createWindow("Edit", new AddWebsiteView(website, update -> {
					addWebsite(update);
				}), EWindowSize.Normal, true);
				break;
			}

		}, "Action", "Check", "Update", "", "Edit", "Delete");
	}

	private void execute(CertWebsite website, ELinuxActions action) {
		// TODO: Checke alle Zertifikate
//		UtilsWindow.createWindow("Multi-Console",
//				new MultiHostConsole(Stream.of(website).collect(Collectors.toSet()), true, action.getCmd()),
//				EWindowSize.Wide, true);
	}

	public Thread runWhileAttached(final Component component, final Runnable task, final long interval,
			final long initialPause, boolean Synchronized) {

		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(initialPause);

					while (component.getUI() != null && component.getUI().isAttached()) {
						if (Synchronized) {
							component.getUI().accessSynchronously(task);
						} else {
							try {
								Future<Void> future = component.getUI().access(task);
								future.get();
							} catch (Exception e) {
								return;
							}

						}
						if (interval <= 0) {
							break;
						}
						Thread.sleep(interval);

					}
				} catch (Exception e) {
				}
			}

		};
		thread.start();
		return thread;
	}
}
