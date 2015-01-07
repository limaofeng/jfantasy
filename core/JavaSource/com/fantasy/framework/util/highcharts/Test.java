package com.fantasy.framework.util.highcharts;

import com.fantasy.framework.util.jackson.JSON;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
	@Resource
	public static void main(String[] args) {
		Chart chart = new Chart("VONB-Daily柱图");
		chart.setCategories(new String[] { "JAN", "FEB" });

		Serie serie = new Serie("只有柱状图", "column");
		serie.setData(new float[] { 55.110001F, 21.629999F, 11.94F, 7.15F, 2.14F });
		chart.addSerie(serie);

		Serie spline = new Serie("柱状图，曲线图", "column");
		spline.setData(new float[] { 55.110001F, 21.629999F, 11.94F, 7.15F, 2.14F });
		chart.addSerie(spline);

		Serie pie = new Serie("饼图", "pie");
		String[] names = { "a", "b", "c", "d", "e" };
		float[] ds = { 55.110001F, 21.629999F, 11.94F, 7.15F, 2.14F };
		for (int i = 0; i < 5; i++) {
			pie.addData(new Data(names[i], ds[i]));
		}
		((Data) pie.getData().get(0)).setSelected(true);
		chart.addSerie(pie);

		System.out.println(JSON.serialize(chart));

		System.out.println(addMonth(-3, null));
	}

	static String addMonth(int mon, String formatStr) {
		if ((formatStr == null) || (formatStr.length() <= 0)){
            formatStr = "yyyyMMdd";
        }
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Calendar cal = Calendar.getInstance();
		Date d = new Date();
		cal.setTime(d);
		cal.add(2, mon);

		return sdf.format(cal.getTime());
	}
}