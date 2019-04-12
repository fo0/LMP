package com.fo0.lmp.ui.templates;

import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.model.Website;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.vaadin.data.Binder;

public class AddWebsiteView extends AVerticalView {

	private static final long serialVersionUID = -1341889021117026811L;

	private Binder<Website> binder = new Binder<Website>(Website.class);

	private MTextField label = new MTextField("Label").withFullWidth();
	private MTextField url = new MTextField("Url").withFullWidth();
	private MTextField port = new MTextField("Port").withFullWidth();
	private MCheckBox active = new MCheckBox("Active");
	private MCheckBox status = new MCheckBox("Status");
	private MTextField daysleft = new MTextField("Daysleft").withFullWidth();

	private DataListener<Website> listener;

	public AddWebsiteView(Website Website, DataListener<Website> listener) {
		super();
		this.listener = listener;
		binder.setBean(Website);
		initBuild();
	}

	public AddWebsiteView() {
		super();
		binder.setBean(Website.builder().build());
		initBuild();
	}

	@Override
	public void build() {
		addComponents(label, url, port, active, status, daysleft);
		addComponents(UtilsComponents.Button_Apply_Discard_Layout(ok -> {
			listener.event(binder.getBean());
			UtilsNotification.notificationTray("Added Node", binder.getBean().toString());
			Utils.closeWindow(AddWebsiteView.this);
		}, discard -> {
			UtilsNotification.discard();
		}));
	}

	@Override
	public void init() {
		binder.forField(label).bind(Website::getLabel, Website::setLabel);
		binder.forField(url).bind(Website::getUrl, Website::setUrl);
		binder.forField(port).withConverter(Integer::valueOf, String::valueOf).bind(Website::getPort, Website::setPort);
		binder.forField(active).bind(Website::isActive, Website::setActive);
		binder.forField(status).bind(Website::isStatus, Website::setStatus);
		binder.forField(daysleft).withConverter(Integer::valueOf, String::valueOf).bind(Website::getDaysleft,
				Website::setDaysleft);
	}
}
