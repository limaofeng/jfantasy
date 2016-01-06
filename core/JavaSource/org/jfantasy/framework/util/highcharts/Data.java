package org.jfantasy.framework.util.highcharts;

import org.jfantasy.framework.util.common.ObjectUtil;
import java.math.BigDecimal;

public class Data {
	private BigDecimal x;
	private BigDecimal y;
	private String name;
	private boolean sliced;
	private boolean selected;

	public Data() {
	}

	public Data(String name, float f) {
		this.name = name;
		setY(f);
	}

	public Data(String name, String p, float f) {
		setName(name);
		if ("y".equals(p)){
            setY(f);
        }else {
            setX(f);
        }
	}

	public Data(String name, String p, float f, boolean sel) {
		setName(name);
		setSliced(sel);
		setSelected(sel);
		if ("y".equals(p)){
            setY(f);
        }else{
            setX(f);
        }
	}

	public Data(BigDecimal f) {
		setY(f);
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public BigDecimal getX() {
		if (ObjectUtil.isNotNull(this.x)){
            return this.x.setScale(2, 6);
        }
		return null;
	}

	public void setX(float x) {
		this.x = BigDecimal.valueOf(x);
	}

	public BigDecimal getY() {
		if (ObjectUtil.isNotNull(this.y)){
            return this.y.setScale(2, 6);
        }
		return null;
	}

	public void setY(float y) {
		this.y = BigDecimal.valueOf(y);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getSliced() {
		return this.sliced;
	}

	public void setSliced(boolean sliced) {
		this.sliced = sliced;
	}

	public boolean getSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		this.sliced = selected;
	}
}