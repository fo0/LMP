package com.fo0.lmp.ui.views.key;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.KeyLoader;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.model.Key;
import com.fo0.lmp.ui.templates.AddKeyView;
import com.fo0.lmp.ui.templates.GridKeys;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

public class KeyView extends AVerticalView {

	private static final long serialVersionUID = 8205214509304261361L;

	private GridKeys grid = null;

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

		grid = new GridKeys(KeyLoader.load());
		grid.withFullSize();
	}

	private MHorizontalLayout createButtonLayout() {
		MHorizontalLayout layout = new MHorizontalLayout();
		layout.add(createButton("Key", MaterialIcons.ADD, e -> {
			UtilsWindow.createWindow("Add Key", new AddKeyView(Key.builder().build(), save -> {
				grid.addKey(save);
			}), EWindowSize.Normal, true);
		}));

		return layout;
	}

	private MButton createButton(String caption, Resource icon, ClickListener listener) {
		return new MButton(caption).withIcon(icon).withListener(listener);
	}

}
