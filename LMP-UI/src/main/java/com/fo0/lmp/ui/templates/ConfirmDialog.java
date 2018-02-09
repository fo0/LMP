package com.fo0.lmp.ui.templates;

import java.io.Serializable;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MWindow;

import com.fo0.lmp.ui.utils.UtilsComponents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class ConfirmDialog extends MWindow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2786228108197021837L;

	public ConfirmDialog(String caption, ClickListener listenerOk, ClickListener listenerDiscard) {
		setCaption(caption);
		setModal(false);
		withWidth(400, Unit.PIXELS).withHeight(150, Unit.PIXELS);
		setContent(new MHorizontalLayout(UtilsComponents.Button_Apply_Discard_Layout(ok -> {
			listenerOk.buttonClick(ok);
			close();
		}, discard -> {
			listenerDiscard.buttonClick(discard);
			close();
		}).withSpacing(true)).withMargin(true).withSize(MSize.FULL_SIZE).alignAll(Alignment.MIDDLE_CENTER));
		UI.getCurrent().addWindow(this);
	}

	public ConfirmDialog(String windowcaption, String okButtonCaption, ClickListener listenerOk,
			ClickListener listenerDiscard) {
		setCaption(windowcaption);
		setModal(false);
		withWidth(400, Unit.PIXELS).withHeight(150, Unit.PIXELS);
		setContent(
				new MHorizontalLayout(UtilsComponents.Button_Apply_Discard_Layout(okButtonCaption, null, ok -> {
					listenerOk.buttonClick(ok);
					close();
				}, discard -> {
					listenerDiscard.buttonClick(discard);
					close();
				}).withSpacing(true)).withMargin(true).withSize(MSize.FULL_SIZE).alignAll(Alignment.MIDDLE_CENTER));
		UI.getCurrent().addWindow(this);
	}
	
	public ConfirmDialog(String windowcaption, String okButtonCaption, ClickListener listenerOk,
			ClickListener listenerDiscard, boolean modal) {
		setCaption(windowcaption);
		setModal(modal);
		withWidth(400, Unit.PIXELS).withHeight(150, Unit.PIXELS);
		setContent(
				new MHorizontalLayout(UtilsComponents.Button_Apply_Discard_Layout(okButtonCaption, null, ok -> {
					listenerOk.buttonClick(ok);
					close();
				}, discard -> {
					listenerDiscard.buttonClick(discard);
					close();
				}).withSpacing(true)).withMargin(true).withSize(MSize.FULL_SIZE).alignAll(Alignment.MIDDLE_CENTER));
		UI.getCurrent().addWindow(this);
	}
	

}
