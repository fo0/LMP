package com.fo0.lmp.ui.templates;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.vaadin.viritin.grid.MGrid;

import com.fo0.lmp.ui.data.KeyLoader;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Key;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;

public class GridKeys extends MGrid<Key> {

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

		addComponentColumn(e -> {
			return addActionButton(e);
		}).setId("menu").setCaption("Action").setWidth(150);

		setColumns("label", "menu");
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
		KeyLoader.save(list);
	}

	public void addKey(Key key, boolean save) {
		list.remove(key);
		list.add(key);
		getDataProvider().refreshAll();
		if (save)
			KeyLoader.save(list);
	}

	public void removekey(Key key) {
		list.remove(key);
		getDataProvider().refreshAll();
		KeyLoader.save(list);
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

		}, "", "Delete");
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
