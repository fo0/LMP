package com.fo0.lmp.ui.utils;

public enum ETheme {

	Dark("themedark"),

	Bright("themebright");

	private String theme;

	private ETheme(String theme) {
		this.theme = theme;
	}

	public String getTheme() {
		return theme;
	}
}
