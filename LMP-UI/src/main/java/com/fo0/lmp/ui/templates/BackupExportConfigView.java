package com.fo0.lmp.ui.templates;

import java.io.ByteArrayInputStream;

import org.vaadin.simplefiledownloader.SimpleFileDownloader;
import org.vaadin.viritin.button.MButton;

import com.fo0.lmp.ui.abstracts.AVerticalView;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

public class BackupExportConfigView extends AVerticalView {

	private static final long serialVersionUID = 7893723017592931162L;

	private TextArea area = new TextArea();
	private MButton dlBtn = null;
	private String config = "";
	private String configFileName = "config.cfg";

	public BackupExportConfigView(String config, String configFileName) {
		super();
		this.config = config;
		this.configFileName = configFileName;
		initBuild();
	}

	@Override
	public void build() {
		area.setValue(config);
		add(dlBtn);
		add(area);
		expand(area);
	}

	private MButton createDownloadButton() {
		SimpleFileDownloader downloader = new SimpleFileDownloader();
		addExtension(downloader);

		MButton button = new MButton("Download Config").withStyleName(ValoTheme.BUTTON_FRIENDLY);
		button.addClickListener(event -> {
			final StreamResource resource = new StreamResource(() -> {
				return new ByteArrayInputStream(config.getBytes());
			}, configFileName);

			downloader.setFileDownloadResource(resource);
			downloader.download();
		});
		return button;
	}

	@Override
	public void init() {
		setSizeFull();
		area.setSizeFull();
		dlBtn = createDownloadButton();
	}

}
