package com.fo0.lmp.ui.templates;

import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.interfaces.DataListener;
import com.fo0.lmp.ui.model.Action;
import com.fo0.lmp.ui.utils.Utils;
import com.fo0.lmp.ui.utils.UtilsComponents;
import com.fo0.lmp.ui.utils.UtilsNotification;
import com.vaadin.data.Binder;
import com.vaadin.ui.TextArea;

public class AddActionView extends AVerticalView {

	private static final long serialVersionUID = 3550310889807381671L;

	private Binder<Action> binder = new Binder<Action>(Action.class);

	private MTextField id = new MTextField("ID").withFullWidth();
	private TextArea command = new TextArea("Command");
	private MTextField description = new MTextField("Description").withFullWidth();
	private MCheckBox active = new MCheckBox("Active");

	private DataListener<Action> listener;

	public AddActionView(Action Action, DataListener<Action> listener) {
		super();
		this.listener = listener;
		binder.setBean(Action);
		initBuild();
	}

	public AddActionView() {
		super();
		binder.setBean(Action.builder().build());
		initBuild();
	}

	@Override
	public void build() {
		addComponents(id, description, active, active, command);
		addComponents(UtilsComponents.Button_Apply_Discard_Layout(ok -> {
			listener.event(binder.getBean());
			UtilsNotification.notificationTray("Added Action", binder.getBean().toString());
			Utils.closeWindow(AddActionView.this);
		}, discard -> {
			UtilsNotification.discard();
		}));
		expand(command);
	}

	@Override
	public void init() {
		command.setWidth(100, Unit.PERCENTAGE);

		binder.forField(id).bind(Action::getId, Action::setId);
		binder.forField(command).bind(Action::getCommand, Action::setCommand);
		binder.forField(description).bind(Action::getDescription, Action::setDescription);
		binder.forField(active).bind(Action::isActive, Action::setActive);
	}

}
