package com.fo0.lmp.ui.templates;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.vaadin.alump.materialicons.MaterialIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.data.KeyManager;
import com.fo0.lmp.ui.enums.ELinuxActions;
import com.fo0.lmp.ui.enums.EWindowSize;
import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.model.Host;
import com.fo0.lmp.ui.model.Key;
import com.fo0.lmp.ui.ssh.SSHClient;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.fo0.lmp.ui.utils.UtilsWindow;
import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;

public class AddHostView extends AVerticalView {

	private static final long serialVersionUID = 3550310889807381671L;

	private Binder<Host> binder = new Binder<Host>(Host.class);

	private MTextField label = new MTextField("Label").withFullWidth();
	private MTextField address = new MTextField("Address").withFullWidth();
	private MTextField port = new MTextField("Port").withFullWidth();
	private MTextField username = new MTextField("Username").withFullWidth();
	private MTextField password = new MTextField("Password").withFullWidth();
	private ComboBox<Key> key = new ComboBox<Key>("Keys");
	private MCheckBox active = new MCheckBox("Active");

	private DataListener<Host> listener;

	public AddHostView(Host host, DataListener<Host> listener) {
		super();
		this.listener = listener;
		binder.setBean(host);
		initBuild();
	}

	public AddHostView() {
		super();
		binder.setBean(Host.builder().build());
		initBuild();
	}

	@Override
	public void build() {
		addComponents(label, address, port, username, password, key, active);
		label.addBlurListener(e -> {
			if (address.getValue().isEmpty())
				address.setValue(label.getValue());
		});
		addComponents(UtilsComponents.Button_Apply_Discard_Layout(ok -> {
			listener.event(binder.getBean());
			UtilsNotification.notificationTray("Added Node", binder.getBean().toString());
			Utils.closeWindow(AddHostView.this);
		}, discard -> {
			UtilsNotification.discard();
		}).with(new MButton().withIcon(MaterialIcons.SEARCH).withListener(e -> {
			MultiHostConsole console = new MultiHostConsole(Stream.of(binder.getBean()).collect(Collectors.toSet()),
					true, true, true, ELinuxActions.PING.getCmd());
			UtilsWindow.createWindow("Result of Scan", console, EWindowSize.Wide, true);
		})));

		Set<Key> myKeys = KeyManager.load();
		if (myKeys != null)
			key.setItems(myKeys);
		key.setItemCaptionGenerator(Key::getLabel);
	}

	@Override
	public void init() {
		binder.forField(label).bind(Host::getLabel, Host::setLabel);
		binder.forField(address).bind(Host::getAddress, Host::setAddress);
		binder.forField(port).withConverter(Integer::valueOf, String::valueOf).bind(Host::getPort, Host::setPort);
		binder.forField(username).bind(Host::getUsername, Host::setUsername);
		binder.forField(password).bind(Host::getPassword, Host::setPassword);
		binder.forField(key).bind(Host::getKey, Host::setKey);
		binder.forField(active).bind(Host::isActive, Host::setActive);
	}

}
