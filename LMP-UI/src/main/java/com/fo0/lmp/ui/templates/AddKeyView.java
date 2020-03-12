package com.fo0.lmp.ui.templates;

import java.security.PrivateKey;

import org.vaadin.viritin.fields.MTextField;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.model.Key;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.vaadin.data.Binder;
import com.vaadin.ui.TextArea;

public class AddKeyView extends AVerticalView {

	private static final long serialVersionUID = 8214802509259872528L;

	private Binder<Key> binder = new Binder<Key>(Key.class);

	private MTextField label = new MTextField("Label").withFullWidth();
	private TextArea privateKey = new TextArea("Private Key");
	private TextArea publicKey = new TextArea("Public Key");
	private DataListener<Key> listener;

	public AddKeyView(Key key, DataListener<Key> listener) {
		super();
		this.listener = listener;
		binder.setBean(key);
		initBuild();
	}

	public AddKeyView() {
		super();
		binder.setBean(Key.builder().build());
		initBuild();
	}

	@Override
	public void build() {
		privateKey.setWidth("100%");
		privateKey.setWordWrap(true);
		
		publicKey.setWidth("100%");
		publicKey.setWordWrap(true);
		
		addComponents(label, privateKey, publicKey);

		addComponents(UtilsComponents.Button_Apply_Discard_Layout(ok -> {
			listener.event(binder.getBean());
			UtilsNotification.notificationTray("Added Node", binder.getBean().toString());
			Utils.closeWindow(AddKeyView.this);
		}, discard -> {
			UtilsNotification.discard();
		}));

	}

	@Override
	public void init() {
		binder.forField(label).bind(Key::getLabel, Key::setLabel);
		binder.forField(privateKey).bind(Key::getPrivateKey, Key::setPrivateKey);
		binder.forField(publicKey).bind(Key::getPublicKey, Key::setPublicKey);

	}
}
