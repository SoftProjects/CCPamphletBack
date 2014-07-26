package com.comicon.pamphlet.data.model;

import java.util.List;

public interface CircleModel extends ItemModel{
	public String getName();
	public String getOrder();
	public String getSite();
	public String getProperty();
	public String getSortLetters() ;
	public boolean isFavorite();
	public List<WorkModel> getWorks();
	public void setFavoirite(boolean b);
	public int getCid();
}
