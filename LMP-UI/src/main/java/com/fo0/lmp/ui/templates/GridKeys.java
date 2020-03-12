package com.fo0.lmp.ui.templates;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.vaadin.viritin.grid.MGrid;

import com.fo0.lmp.ui.data.KeyManager;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.Key;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;

public class GridKeys extends MGrid<Key> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -949632134596892202L;
	private Set<Key> list = new HashSet<Key>();

	public GridKeys(Set<Key> keys) {
		super(Key.class);

		if (list != null)
			this.list = keys;

		build();
	}

	private void build() {
		try {
			setItems(list);
		} catch (Exception e2) {
			list = new HashSet<Key>();
			setItems(list);
		}

		addColumn(e -> "Linux").setId("os").setCaption("Platform");

//		addComponentColumn(e -> {
//			MHorizontalLayout labels = new MHorizontalLayout();
//			if (e.getHost() != null) {
//				labels.add(new MLabel().withValue(ICON.HOST.getHtml()).withContentMode(ContentMode.HTML));
//			}
//
//			if (e.getAction() != null) {
//				labels.add(new MLabel().withValue(ICON.ACTION.getHtml()).withContentMode(ContentMode.HTML));
//			}
//
//			return labels;
//		}).setId("info").setCaption("Info");

		addComponentColumn(e -> {
			return addActionButton(e);
		}).setId("menu").setCaption("Action");

		// setColumns("label", "port", "activecheck", "statuscheck", "daysleft", "info",
		// "menu");
		setColumns("label", "menu");
		// sort("url", SortDirection.ASCENDING);
	}

	public void setList(Set<Key> list) {
		this.list = list;
	}

	public Set<Key> getList() {
		return list;
	}

	public void addKey(Key key) {
		list.remove(key);
		list.add(key);
		getDataProvider().refreshAll();
		KeyManager.save(list);
	}

	public void addKey(Key key, boolean save) {
		list.remove(key);
		list.add(key);
		getDataProvider().refreshAll();
		if (save)
			KeyManager.save(list);
	}

	public void removekey(Key key) {
		list.remove(key);
		getDataProvider().refreshAll();
		KeyManager.save(list);
	}

	private void execute(Host host, Action action) {
		UtilsWindow.createWindow("Multi-Console",
				new MultiHostConsole(Stream.of(host).collect(Collectors.toSet()), true, action.getCommand()),
				EWindowSize.Wide, true);
	}

	private MenuBar addActionButton(Key key) {
		return UtilsComponents.multiActionButton(cmd -> {

			switch (cmd.getText()) {
			case "Delete":
				new ConfirmDialog("Delete key: " + key.getLabel(), ok -> {
					removekey(key);
				}, discard -> {
					removekey(key);
				});

				break;

			case "Edit":
				UtilsWindow.createWindow("Edit", new AddKeyView(key, update -> {
					addKey(update);
				}), EWindowSize.Normal, true);
				break;
			}

		}, "Edit", "Delete");
	}

	private void execute(Key key, ELinuxActions action) {
		// TODO: Checke alle Zertifikate
//		UtilsWindow.createWindow("Multi-Console",
//				new MultiHostConsole(Stream.of(key).collect(Collectors.toSet()), true, action.getCmd()),
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
