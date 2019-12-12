package com.fo0.lmp.ui.templates;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.fo0.lmp.ui.data.AccountManager;
import com.fo0.lmp.ui.utils.ETheme;
import com.fo0.lmp.ui.utils.STYLES;

public class HostConsole extends MVerticalLayout {

	private static final long serialVersionUID = 3330690340036786599L;

	private AceEditor console = null;
	private boolean autoScroll = true;

	public HostConsole() {
		console = createEditor();
		withSpacing(false).withMargin(false);
		addComponents(console);
		expand(console);
	}

	private AceEditor createEditor() {
		AceEditor console = new AceEditor();
		if (AccountManager.load().getTheme() == ETheme.Dark)
			console.setTheme(AceTheme.tomorrow_night_eighties);
		console.setMode(AceMode.sh);
		console.addStyleName(STYLES.BORDER_NONE);
		console.setValue("");
		console.setFontSize("14px");
		console.setSizeFull();
		console.setHeight("100%");
		console.setWordWrap(false);
		console.setReadOnly(true);
		return console;
	}

	public AceEditor getConsole() {
		return console;
	}

	public void setAutoScroll(boolean autoScroll) {
		this.autoScroll = autoScroll;
	}

	public void clear() {
		console.setValue("");
	}

	public void append(String value) {
		console.setReadOnly(false);
		console.setValue(console.getValue() + value);
		console.setReadOnly(true);
		if (autoScroll) {
			console.scrollToRow(Integer.MAX_VALUE);
		}
	}

}
