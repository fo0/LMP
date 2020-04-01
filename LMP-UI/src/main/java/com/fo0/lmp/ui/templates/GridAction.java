package com.fo0.lmp.ui.templates;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.grid.MGrid;

import com.fo0.lmp.ui.data.ActionLoader;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.utils.STYLES;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.renderers.HtmlRenderer;

public class GridAction extends MGrid<Action> {

	private static final long serialVersionUID = 9012141701348974970L;

	private Set<Action> list = new HashSet<Action>();

	public GridAction(Set<Action> Actions) {
		super(Action.class);
		if (list != null)
			this.list = Actions;
		build();
	}

	private void build() {
		try {
			setItems(list);
		} catch (Exception e2) {
			list = new HashSet<Action>();
			setItems(list);
		}

		addColumn(e -> StringUtils.abbreviate(e.getCommand(), 70)).setId("shortcmd").setCaption("Command");

		addColumn(e -> {
			if (e.isActive()) {
				return FontAwesome.CHECK.getHtml();
			} else {
				return FontAwesome.TIMES.getHtml();
			}
		}, new HtmlRenderer()).setId("activecheck").setCaption("Active").setStyleGenerator(e -> {
			if (e.isActive()) {
				return STYLES.ICON_GREEN;
			} else {
				return STYLES.ICON_RED;
			}
		});

		addComponentColumn(e -> {
			return addActionButton(e);
		}).setId("action").setCaption("Action");

		setColumns("description", "shortcmd", "activecheck", "action");
	}

	public void setList(Set<Action> list) {
		this.list = list;
	}

	public Set<Action> getList() {
		return list;
	}

	public void add(Action Action) {
		list.remove(Action);
		list.add(Action);
		getDataProvider().refreshAll();
		ActionLoader.save(list);
	}

	public void add(Action Action, boolean save) {
		list.remove(Action);
		list.add(Action);
		getDataProvider().refreshAll();
		if (save)
			ActionLoader.save(list);
	}

	public void remove(Action Action) {
		list.remove(Action);
		getDataProvider().refreshAll();
		ActionLoader.save(list);
	}

	private MenuBar addActionButton(Action Action) {
		return UtilsComponents.multiActionButton(cmd -> {

			switch (cmd.getText()) {
			case "Delete":
				remove(Action);
				break;

			case "Edit":
				UtilsWindow.createWindow("Edit", new AddActionView(Action, update -> {
					add(update);
				}), EWindowSize.Normal, true);
				break;
			}

		}, "Action", "Edit", "", "Delete");
	}

}
