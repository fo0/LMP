package com.fo0.lmp.ui.templates;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;
import org.vaadin.viritin.grid.MGrid;

import com.fo0.lmp.ui.collector.Collector;
import com.fo0.lmp.ui.collector.hostinfo.HostInfoCollector;
import com.fo0.lmp.ui.data.HostLoader;
import com.fo0.lmp.ui.data.HostPropertyLoader;
import com.fo0.lmp.ui.enums.EHostProperty;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.manager.HostPropertyManager;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.HostProperty;
import com.fo0.lmp.ui.utils.STYLES;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.renderers.HtmlRenderer;

public class GridHosts extends MGrid<Host> {

	private static final long serialVersionUID = 9012141701348974970L;

	private Set<Host> list = new HashSet<Host>();

	public GridHosts(Set<Host> hosts) {
		super(Host.class);
		list = hosts;
		setItems(list);
		build();
	}

	private void build() {
		Map<String, HostProperty> properties = HostPropertyLoader.load().stream()
				.collect(Collectors.toMap(key -> key.getId(), value -> value));

		addColumn(e -> {
			HostProperty p = properties.get(e.getId());
			return p != null ? p.getManagedProperty(EHostProperty.Hostname) : Strings.EMPTY;
		}).setId("hostproperty.hostname").setCaption("Hostname");

		addColumn(e -> {
			return e.isActive() ? FontAwesome.CHECK.getHtml() : FontAwesome.TIMES.getHtml();
		}, new HtmlRenderer()).setId("activecheck").setCaption("Active").setStyleGenerator(e -> {
			return e.isActive() ? STYLES.ICON_GREEN : STYLES.ICON_RED;
		});

		addColumn(e -> {
			return e.isReachable() ? FontAwesome.CHECK.getHtml() : FontAwesome.TIMES.getHtml();
		}, new HtmlRenderer()).setId("status").setCaption("Status").setStyleGenerator(e -> {
			return e.isReachable() ? STYLES.ICON_GREEN : STYLES.ICON_RED;
		});

		addComponentColumn(e -> {
			return addActionButton(e);
		}).setId("action").setCaption("Action");

		setColumns("label", "address", "hostproperty.hostname", "port", "status", "activecheck", "action");
	}

	public void setList(Set<Host> list) {
		this.list = list;
		setItems(list);
	}

	public Set<Host> getList() {
		return list;
	}

	public void refresh() {
		setList(HostLoader.load());
		getDataProvider().refreshAll();
	}

	public void addHost(Host host) {
		list.remove(host);
		list.add(host);
		getDataProvider().refreshAll();
		HostLoader.save(list);
	}

	public void addHost(Host host, boolean save) {
		list.remove(host);
		list.add(host);
		getDataProvider().refreshAll();
		if (save)
			HostLoader.save(list);
	}

	public void removeHost(Host host) {
		list.remove(host);
		getDataProvider().refreshAll();
		HostLoader.deleteById(host.getId());
		HostPropertyManager.deletePropertyById(host.getId());
	}

	private MenuBar addActionButton(Host host) {
		return UtilsComponents.multiActionButton(cmd -> {

			switch (cmd.getText()) {
			case "Delete":
				new ConfirmDialog("Delete Host: " + host.getLabel(), ok -> {
					removeHost(host);
				}, discard -> {
				});

				break;

			case "Update & Upgrade":
				execute(host, ELinuxActions.APT_UPDATE_AND_DIST_UPGRADE);
				break;

			case "Ping":
				execute(host, ELinuxActions.PING);
				break;

			case "Custom":
				execute(host, ELinuxActions.CUSTOM);
				break;

			case "Properties":
				UtilsWindow.createWindow("Edit",
						new GridHostProperty(HostPropertyLoader.getHostPropertyByIdOrCreate(host.getId()))
								.withFullSize(),
						EWindowSize.Normal, true);
				break;

			case "Edit":
				UtilsWindow.createWindow("Edit", new AddHostView(host, update -> {
					new HostInfoCollector(update).collectAndSaveResult();
				}), EWindowSize.Normal, true);
				break;

			case "Collect Informations":
				Collector.collect(host);
				break;
			}

		}, "Action", "Update & Upgrade", "Custom", "", "Collect Informations", "Properties", "", "Edit", "Delete");
	}

	private void execute(Host host, ELinuxActions action) {
		UtilsWindow.createWindow("Multi-Console",
				new MultiHostConsole(Stream.of(host).collect(Collectors.toSet()), true, action.getCmd()),
				EWindowSize.Wide, true);
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
