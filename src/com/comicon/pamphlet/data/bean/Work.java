package com.comicon.pamphlet.data.bean;

import com.comicon.pamphlet.data.model.CircleModel;
import com.comicon.pamphlet.data.model.WorkModel;

public class Work implements WorkModel {
	private CircleModel circle;
	private int wid;
	private String name;
	private String mode;
	private String category;
	private String theme;
	private String price;
	private String sample;
	public Work(CircleModel circle,int wid,String name,String mode,String category,String theme,String price,String sample){
		this.circle = circle;
		this.wid = wid;
		this.name = name;
		this.mode = mode;
		this.category = category;
		this.theme = theme;
		price = price.replace("RMB", "");
		price = price.replace("rmb", "");
		price = price.replace("元", "");
		this.price = price;
		if(!sample.startsWith("http:"))sample = "http://"+sample;
		this.sample = sample;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public CircleModel getCircle() {
		return circle;
	}

	@Override
	public String getPrice() {
		return price;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getTheme() {
		return theme;
	}

	@Override
	public String getSample() {
		return sample;
	}
}
