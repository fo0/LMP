package com.fo0.lmp.ui.templates;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;

import com.fo0.lmp.ui.utils.STYLES;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.themes.ValoTheme;

public class VaadinUploader extends Upload implements Upload.ProgressListener, Upload.SucceededListener,
		Upload.StartedListener, Upload.FailedListener, Upload.FinishedListener, Upload.ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1252650154701055827L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private UploadBufferReceiver receiver = new UploadBufferReceiver();
	private ProgressBar progressBar = new ProgressBar(0f);
	private MTextField txtFile = new MTextField().withPlaceholder("Choose a File");
	private MButton stopButton = new MButton().withIcon(VaadinIcons.STOP).withFullWidth()
			.withStyleName(ValoTheme.BUTTON_DANGER);
	private MButton startButton = new MButton().withIcon(VaadinIcons.PLAY).withFullWidth()
			.withStyleName(ValoTheme.BUTTON_FRIENDLY);

	private StartListener startListener = null;
	private StopListener stopListener = null;
	private FinishListener finishListener = null;
	private boolean failed = false;

	private boolean simulate_slow_upload = false;

	public VaadinUploader() {
		init();
		receiver.setSlowSimulation(simulate_slow_upload);
		setImmediateMode(true);
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public MTextField getTextFieldFileName() {
		return txtFile;
	}

	public MButton getStartButton() {
		return startButton;
	}

	public MButton getStopButton() {
		return stopButton;
	}

	public void init() {
		progressBar.setWidth(100, Unit.PERCENTAGE);
		progressBar.addStyleName(STYLES.DYNAMIC_PROGRESS_BAR);

		setStyleName(STYLES.FILE_CHOOSER_BUTTON_INVIS);

		setReceiver(receiver);
		setImmediateMode(false);
		addProgressListener(this);
		addStartedListener(this);
		addSucceededListener(this);
		addFailedListener(this);
		addFinishedListener(this);
		addChangeListener(this);
		progressBar.setVisible(false);

		startButton.addClickListener(e -> {
			if (startListener != null)
				if (startListener.action())
					submitUpload();
				else
					return;

			submitUpload();
		});
		stopButton.addClickListener(e -> {
			if (stopListener != null)
				if (stopListener.action())
					interruptUpload();
				else
					return;

			interruptUpload();
		});

		toggleVisibilityOfButtons(false);
		txtFile.setReadOnly(true);
	}

	public void addStartListener(StartListener l) {
		startListener = l;
	}

	public void addStopListener(StopListener l) {
		stopListener = l;
	}

	public void addFinishListener(FinishListener listener) {
		finishListener = listener;
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		logger.info("Failed Upload: " + event.getFilename());
		toggleVisibilityProgressBar(false);
		progressBar.setValue(0f);
		txtFile.clear();
		receiver.clear();
		toggleVisibilityOfButtons(false);
		addStyleName(STYLES.VISIBLE);
		failed = true;
	}

	@Override
	public void uploadFinished(FinishedEvent event) {
		if (failed) {
			logger.debug("detected failed upload, skipping finished listener execution");
			addStyleName(STYLES.VISIBLE);
			failed = false;
			return;
		}

		logger.info("Finished Upload: " + event.getFilename());
		logger.info("Bytes: " + event.getLength());
		logger.info("exptected Bytes: " + event.getUpload().getUploadSize());
		logger.info("received Bytes: " + event.getUpload().getBytesRead());
		logger.info("my receiver: " + receiver.getFileName());
		logger.info("my receiver content: " + receiver.getStream());
		addStyleName(STYLES.VISIBLE);
		if (finishListener != null && !failed)
			finishListener.action(receiver);

	}

	@Override
	public void uploadStarted(StartedEvent event) {
		toggleVisibilityProgressBar(true);
		toggleVisibilityOfButtons(false);
		stopButton.setVisible(true);
		addStyleName(STYLES.INVISIBLE);
	}

	private void toggleVisibilityOfButtons(boolean visible) {
		startButton.setVisible(visible);
		stopButton.setVisible(visible);
	}

	private void toggleVisibilityProgressBar(boolean visible) {
		progressBar.setVisible(visible);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		toggleVisibilityProgressBar(false);
		toggleVisibilityOfButtons(false);
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		progressBar.setValue(readBytes / (float) contentLength);
	}

	@Override
	public void filenameChanged(ChangeEvent event) {
		txtFile.setReadOnly(false);
		txtFile.setValue(event.getFilename());
		progressBar.setValue(0f);
		toggleVisibilityProgressBar(true);
		toggleVisibilityOfButtons(true);
		txtFile.setReadOnly(true);
	}

	public interface StartListener {

		public boolean action();
	}

	public interface StopListener {

		public boolean action();
	}

	public interface FinishListener {

		public void action(UploadBufferReceiver event);
	}
}