package com.fo0.lmp.ui.utils;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.RangeSelector;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;

public class UtilsCharts {

	public static Chart createTimeLine(String caption, Series seriesPrice) {
		final Chart chart = new Chart();
		chart.setTimeline(true);
		Configuration configuration = chart.getConfiguration();
		configuration.setSeries(seriesPrice);
		GradientColor color = GradientColor.createLinear(0, 0, 0, 1);
		color.addColorStop(0, new SolidColor("#444444"));
		color.addColorStop(1, new SolidColor("#000000"));
		configuration.getChart().setBackgroundColor(color);
		configuration.setTitle(caption);
		RangeSelector rangeSelector = new RangeSelector();
		rangeSelector.setSelected(1);
		configuration.setRangeSelector(rangeSelector);
		chart.drawChart(configuration);
		return chart;
	}

	public static Chart createSplineChart(String caption, Series series) {
		Chart chart = new Chart();
		Configuration configuration = chart.getConfiguration();
		configuration.setSeries(series);
		GradientColor color = GradientColor.createLinear(0, 0, 0, 1);
		color.addColorStop(0, new SolidColor("#444444"));
		color.addColorStop(1, new SolidColor("#000000"));
		configuration.getChart().setBackgroundColor(color);
		configuration.getChart().setType(ChartType.SPLINE);
		configuration.setTitle(caption);
		configuration.getyAxis().setOpposite(true);
		XAxis xAxis = configuration.getxAxis();
		xAxis.setType(AxisType.DATETIME);
		xAxis.setTickPixelInterval(150);
		configuration.addxAxis(xAxis);
		chart.drawChart(configuration);
		return chart;
	}

}
