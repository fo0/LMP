package com.fo0.lmp.ui.main;

import com.fo0.lmp.ui.utils.ETheme;
import com.fo0.vaadin.browserwindowopener.main.BrowserPopupUI;

public class PopupUI extends BrowserPopupUI {

	private static final long serialVersionUID = 6082088797812786144L;

	public PopupUI() {
		super();
		setTheme(ETheme.valueOf(getParams("theme")).getTheme());
	}

}
