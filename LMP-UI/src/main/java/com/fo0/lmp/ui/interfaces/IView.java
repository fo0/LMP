package com.fo0.lmp.ui.interfaces;

import com.fo0.lmp.ui.controller.Controller;

public interface IView {

	public void build();

	public void init();

	public Controller getController();

	// value can be "login" or "logout"
	public void resetGUI();

	public String getNameView();
	
}
