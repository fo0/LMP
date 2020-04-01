package com.fo0.lmp.ui.templates;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fo0.fcf.utils.model.KeyValue;

import org.vaadin.viritin.grid.MGrid;

import com.fo0.lmp.ui.model.HostProperty;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.vaadin.ui.MenuBar;

public class GridHostProperty extends MGrid<KeyValue> {

	private static final long serialVersionUID = 9012141701348974970L;

	private Set<KeyValue> list = new HashSet<KeyValue>();

	public GridHostProperty(HostProperty hosts) {
		super(KeyValue.class);
		list = hosts.getProperties().entrySet().stream()
				.map(e -> KeyValue.builder().key(e.getKey()).value(e.getValue()).build()).collect(Collectors.toSet());
		setItems(list);
		build();
	}

	private void build() {
		addComponentColumn(e -> {
			return addActionButton(e);
		}).setId("action").setCaption("Action");

		setColumns("key", "value");
	}

	public void setList(Set<KeyValue> list) {
		this.list = list;
	}

	public Set<KeyValue> getList() {
		return list;
	}

	private MenuBar addActionButton(KeyValue host) {
		return UtilsComponents.multiActionButton(cmd -> {

			switch (cmd.getText()) {
			case "Delete":
				new ConfirmDialog("Delete Host: " + host.getKey(), ok -> {
					list.remove(ok);
					getDataProvider().refreshAll();
				}, discard -> {
				});

				break;
			}

		}, "Delete");
	}
}
