package com.fo0.lmp.ui.utils;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.github.appreciated.material.MaterialTheme;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.themes.ValoTheme;

public class UtilsComponents {

	/**
	 * Creating Multi action button similar to combobox <br>
	 * 
	 * Use empty string "" for splitter items
	 */
	public static MenuBar multiActionButton(Command cmd, String caption, String... items) {
		MenuBar split = new MenuBar();
		split.addStyleName(MaterialTheme.MENUBAR_BORDERLESS);
		MenuBar.MenuItem dropdown = split.addItem(caption, cmd);
		dropdown = split.addItem("", null);
		for (String string : items) {
			if (string.isEmpty()) {
				dropdown.addSeparator();
			} else {
				dropdown.addItem(string, cmd);
			}

		}
		return split;
	}
	
	public static ContextMenu multiActionContextButton(Command cmd, String caption, String... items) {
		
		ContextMenu contextMenu = new ContextMenu(parentComponent, setAsMenuForParentComponent);
		
		
		return null;
	}

	public static MHorizontalLayout Button_Apply_Discard_Layout(ClickListener listenerSave,
			ClickListener listenerDiscard) {
		MButton apply = new MButton("Ok").withStyleName(ValoTheme.BUTTON_FRIENDLY).withIcon(FontAwesome.SAVE)
				.withListener(listenerSave);
		apply.setClickShortcut(KeyCode.ENTER);
		MButton discard = new MButton("Cancel").withStyleName(ValoTheme.BUTTON_DANGER).withIcon(FontAwesome.TIMES)
				.withListener(listenerDiscard);
		discard.setClickShortcut(KeyCode.ESCAPE);
		return new MHorizontalLayout(apply, discard);
	}

	public static MHorizontalLayout Button_Apply_Discard_Layout(String okCaption, String discardCaption,
			ClickListener listenerSave, ClickListener listenerDiscard) {
		MButton apply = new MButton("Ok").withStyleName(ValoTheme.BUTTON_FRIENDLY).withIcon(FontAwesome.SAVE)
				.withListener(listenerSave);
		apply.setClickShortcut(KeyCode.ENTER);
		if (okCaption != null)
			apply.withCaption(okCaption);

		MButton discard = new MButton("Cancel").withStyleName(ValoTheme.BUTTON_DANGER).withIcon(FontAwesome.TIMES)
				.withListener(listenerDiscard);
		discard.setClickShortcut(KeyCode.ESCAPE);
		if (discardCaption != null)
			discard.withCaption(discardCaption);

		return new MHorizontalLayout(apply, discard);
	}

}	
