package com.fo0.lmp.ui.collector.interfaces;

import java.util.Optional;

public interface ICollector<T> {

	public void collect();

	public default Optional<T> collectAndSaveResult() {
		collect();
		mergeAndSave();
		return getResult();
	};

	public default Optional<T> collectAndGetResult() {
		collect();
		return getResult();
	};

	public Optional<T> getResult();

	public void mergeAndSave();

}
