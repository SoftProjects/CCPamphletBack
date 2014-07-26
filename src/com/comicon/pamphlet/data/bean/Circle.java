package com.comicon.pamphlet.data.bean;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;

import com.comicon.pamphlet.data.dataBase.DataBase;
import com.comicon.pamphlet.data.model.CircleModel;
import com.comicon.pamphlet.data.model.WorkModel;

public class Circle implements CircleModel{
	private static final long serialVersionUID = -973008573518072685L;
	private String sortLetters;
	private String name;
	private int cid;
	private String mode;
	private String site;
	private String property;
	private String boothnum;
	private String order;
	private boolean isFavour;

	public Circle(int cid,String name,String mode,String site,String property,String boothnum,String order,boolean isFavour){
		this.cid = cid;
		this.name = name;
		this.mode = mode;
		if(!site.startsWith("http:"))site="http://"+site;
		this.site = site;
		this.property = property;
		this.boothnum = boothnum;
		this.order = order;
		this.isFavour = isFavour;
//		String pinyin = CharacterParser.getInstance().getSelling(name);
//		sortLetters = pinyin.substring(0, 1).toUpperCase();
		sortLetters = (order!=null && !order.equals(""))?order.substring(0,1):"???";
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSortLetters() {
		return sortLetters;
	}

	@Override
	public boolean isFavorite() {
		return isFavour;
	}

	@Override
	public List<WorkModel> getWorks() {
		return DataBase.instance(null).getWorks(this);
	}

	@Override
	public CircleModel getCircle() {
		return this;
	}
	
	@Override
	public void setFavoirite(boolean isFavourate){
		DataBase.instance(null).setFavourite(isFavourate,this);
		this.isFavour = isFavourate;
	}

	@Override
	public int getCid() {
		return cid;
	}

	@Override
	public String getOrder() {
		return order;
	}

	@Override
	public String getSite() {
		return site;
	}

	@Override
	public String getProperty() {
		return property;
	}
}
