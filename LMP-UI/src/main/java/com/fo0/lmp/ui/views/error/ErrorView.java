package com.fo0.lmp.ui.views.error;

import org.vaadin.viritin.label.MLabel;

import com.fo0.lmp.ui.abstracts.AVerticalView;

public class ErrorView extends AVerticalView {

	public ErrorView() {
		super();
		initBuild();
	}

	@Override
	public void build() {
		// TODO Auto-generated method stub
		addComponent(new MLabel("An Error occurred!"));

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
