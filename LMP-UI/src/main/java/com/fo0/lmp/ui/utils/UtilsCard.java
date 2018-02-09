package com.fo0.lmp.ui.utils;

import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.github.appreciated.material.MaterialTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

public class UtilsCard {

	public static MPanel createCard(String caption, Layout layout, String width, String height) {
		return new MPanel(layout != null ? layout : null).withCaption(caption).withSize(width, height)
				.withStyleName(MaterialTheme.CARD_1);
	}

	public static MPanel createCardDefault(String caption, Layout layout) {
		return createCard(caption, layout, "250px", "200px");
	}

	public static MPanel createCard1Info(String caption, Component... layout) {
		return new MPanel(new MVerticalLayout(layout)).withPrimaryStyleName("v-csslayout").withCaption(caption)
				.withStyleName(MaterialTheme.CARD_2).withFullWidth();
	}

	public static MPanel createCard1(String caption, Layout layout) {
		return new MPanel(layout).withCaption(caption).withStyleName(MaterialTheme.CARD_1).withFullWidth();
	}

	public static MPanel createCard1(String caption, MPanel layout) {
		return layout.withCaption(caption).withStyleName(MaterialTheme.CARD_1).withFullWidth();
	}

}
