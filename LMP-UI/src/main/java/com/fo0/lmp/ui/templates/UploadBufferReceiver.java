package com.fo0.lmp.ui.templates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Upload;

public class UploadBufferReceiver implements StreamResource.StreamSource, Upload.Receiver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ByteArrayOutputStream outputBuffer = null;
	private String mimeType;
	private String fileName;
	private boolean slow = false;

	public InputStream getStream() {
		if (outputBuffer == null) {
			return null;
		}
		return new ByteArrayInputStream(outputBuffer.toByteArray());
	}

	/**
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(String, String)
	 */
	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		fileName = filename.trim();
		mimeType = MIMEType;
		outputBuffer = new ByteArrayOutputStream() {
			@Override
			public synchronized void write(byte[] b, int off, int len) {
				simulateSlow();
				super.write(b, off, len);
			}

		};
		return outputBuffer;
	}

	public void clear() {
		logger.debug("clear buffer");
		outputBuffer = null;
		mimeType = "";
		fileName = "";
	}

	public void setSlowSimulation(boolean slow) {
		this.slow = slow;
	};

	private void simulateSlow() {
		if (slow) {
			try {
				TimeUnit.MILLISECONDS.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the fileName.
	 * 
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the mimeType.
	 * 
	 * @return String
	 */
	public String getMimeType() {
		return mimeType;
	}

}
