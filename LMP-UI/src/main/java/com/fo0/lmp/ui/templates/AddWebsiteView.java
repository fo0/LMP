package com.fo0.lmp.ui.templates;

import java.util.Set;

import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.ActionManager;
import com.fo0.lmp.ui.data.LinuxHostManager;
import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.CertWebsite;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;

public class AddWebsiteView extends AVerticalView {

	private static final long serialVersionUID = -1341889021117026811L;

	private Binder<CertWebsite> binder = new Binder<CertWebsite>(CertWebsite.class);

	private MTextField label = new MTextField("Label").withFullWidth();
	private MTextField url = new MTextField("Url").withFullWidth();
	private MTextField port = new MTextField("Port").withFullWidth();
	private MCheckBox active = new MCheckBox("Active");
	private ComboBox<Host> host = new ComboBox<Host>("Host");
	private ComboBox<Action> action = new ComboBox<Action>("action");

	private DataListener<CertWebsite> listener;

	public AddWebsiteView(CertWebsite Website, DataListener<CertWebsite> listener) {
		super();
		this.listener = listener;
		binder.setBean(Website);
		initBuild();
	}

	public AddWebsiteView() {
		super();
		binder.setBean(CertWebsite.builder().build());
		initBuild();
	}

	@Override
	public void build() {
		addComponents(label, url, port, host, action, active);
		label.addBlurListener(e -> {
			if (url.getValue().isEmpty())
				url.setValue(label.getValue());
		});

		addComponents(UtilsComponents.Button_Apply_Discard_Layout(ok -> {
			listener.event(binder.getBean());
			UtilsNotification.notificationTray("Added Node", binder.getBean().toString());
			Utils.closeWindow(AddWebsiteView.this);
		}, discard -> {
			UtilsNotification.discard();
		}));

		Set<Host> myHosts = LinuxHostManager.load();
		if (myHosts != null)
			host.setItems(myHosts);
		host.setItemCaptionGenerator(Host::getLabel);

		Set<Action> myActions = ActionManager.load();
		if (myActions != null)
			action.setItems(myActions);
		action.setItemCaptionGenerator(Action::getDescription);
	}

	@Override
	public void init() {
		binder.forField(label).bind(CertWebsite::getLabel, CertWebsite::setLabel);
		binder.forField(url).bind(CertWebsite::getUrl, CertWebsite::setUrl);
		binder.forField(port).withConverter(Integer::valueOf, String::valueOf).bind(CertWebsite::getPort,
				CertWebsite::setPort);
		binder.forField(active).bind(CertWebsite::isActive, CertWebsite::setActive);
		binder.forField(host).bind(CertWebsite::getHost, CertWebsite::setHost);
		binder.forField(action).bind(CertWebsite::getAction, CertWebsite::setAction);
	}
}
