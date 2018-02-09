package com.fo0.lmp.ui.views.general;

import org.vaadin.viritin.label.MLabel;

import com.fo0.lmp.ui.abstracts.AVerticalView;

public class AccessDeniedView extends AVerticalView {

	@Override
	public void build() {
		addComponent(new MLabel("Access Denied!"));

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
