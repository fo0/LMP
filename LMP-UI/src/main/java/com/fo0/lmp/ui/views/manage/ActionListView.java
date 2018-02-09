package com.fo0.lmp.ui.views.manage;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.ActionManager;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.templates.AddActionView;
import com.fo0.lmp.ui.templates.GridAction;
import com.fo0.lmp.ui.utils.UtilsWindow;

public class ActionListView extends AVerticalView {

	private static final long serialVersionUID = 4273653074117421617L;

	private GridAction grid;

	public ActionListView() {
		initBuild();
	}

	@Override
	public void build() {
		addComponent(createButtons());
		addComponent(grid);
		expand(grid);
	}

	public MHorizontalLayout createButtons() {
		return new MHorizontalLayout(new MButton().withIcon(MaterialIcons.ADD).withListener(e -> {
			UtilsWindow.createWindow("Add Action", new AddActionView(Action.builder().build(), save -> {
				grid.remove(save);
				grid.add(save);
			}), EWindowSize.Normal, true);
		}));
	}

	@Override
	public void init() {
		grid = new GridAction(ActionManager.load());
		grid.setSizeFull();
	}

}
