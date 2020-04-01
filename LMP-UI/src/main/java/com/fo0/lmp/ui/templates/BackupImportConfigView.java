package com.fo0.lmp.ui.templates;

import com.fo0.fcf.logger.LOGSTATE;

import org.apache.commons.io.IOUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.fo0.lmp.ui.templates.VaadinUploader.FinishListener;
import com.fo0.lmp.ui.templates.VaadinUploader.StartListener;
import com.github.appreciated.material.MaterialTheme;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

public class BackupImportConfigView extends AVerticalView {

	private static final long serialVersionUID = 7893723017592931162L;

	private TextArea area = new TextArea();
	private VaadinUploader uploader;
	private MButton save = new MButton("Save").withStyleName(MaterialTheme.BUTTON_DANGER);
	private MCheckBox override = new MCheckBox("Override", false).withStyleName(MaterialTheme.CHECKBOX_LARGE);
	private ImportListener importListener = null;

	public BackupImportConfigView(ImportListener importListener) {
		super();
		this.importListener = importListener;
		initBuild();
	}

	@Override
	public void build() {
		add(new MHorizontalLayout(save, uploader, override));
		add(area);
		expand(area);

		save.addClickListener(e -> {
			importListener.config(area.getValue(), override.getValue());
		});
	}

	private VaadinUploader createUploadButton() {
		VaadinUploader uploader = new VaadinUploader();
		uploader.setButtonCaption("Import-File");
		uploader.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		uploader.addStartListener(new StartListener() {

			@Override
			public boolean action() {
				return true;
			}
		});

		uploader.addFinishListener(new FinishListener() {

			@Override
			public void action(UploadBufferReceiver event) {
				try {
					getLogger().info("Detected Finish Upload: " + event.getFileName() + "." + event.getMimeType());
					getLogger().info("Stream content: " + event.getStream().toString());
					byte[] data = IOUtils.toByteArray(event.getStream());
					area.setValue(new String(data));
				} catch (Exception e) {
					getLogger().error(LOGSTATE.FAILED + "to upload file ", e);
				}

			}
		});

		return uploader;
	}

	@Override
	public void init() {
		setSizeFull();
		area.setSizeFull();

		uploader = createUploadButton();
	}

	public interface ImportListener {
		void config(String config, boolean override);
	}

}
