package com.comicon.pamphlet.data.cotroller;

import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.comicon.pamphlet.data.model.CircleModel;
import com.comicon.pamphlet.data.model.WorkModel;

public interface Resourcer {
	public List<CircleModel> getAllList();
	public List<CircleModel> getFavouriteList();
	public List<WorkModel> getSearchResult(String search);
	public void update(Handler handler);
	public void sendResponse(String s,Handler handler);
	public void initial();
	public void checkUpdate(Handler updateHandler);
	public CircleModel getCircle(int cid);
}
