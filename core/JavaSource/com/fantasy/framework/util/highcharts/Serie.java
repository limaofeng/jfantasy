package com.fantasy.framework.util.highcharts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Serie {
	private String name;
	private String type;
	private List<Data> data = new ArrayList<Data>();

	public Serie(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Data> getData() {
		return this.data;
	}

	public void setData(BigDecimal[] bigDecimals) {
		for (BigDecimal f : bigDecimals)
			addData(new Data(f));
	}

	public void setData(float[] fs) {
		setData("y", fs);
	}

	public void addData(Data data) {
		this.data.add(data);
	}

	public void setData(String p, float[] fs) {
		for (float f : fs)
			addData(new Data(p, f));
	}

	public void setData(String[] names, float[] fs) {
		for (int i = 0; i < names.length; i++)
			addData(new Data(names[i], "y", fs[i]));
	}
}