package com.comicon.pamphlet.data.cotroller;

import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.comicon.pamphlet.data.appsetting.Data;
import com.comicon.pamphlet.data.appsetting.UpdateResult;
import com.comicon.pamphlet.data.dataBase.DataBase;
import com.comicon.pamphlet.data.model.CircleModel;
import com.comicon.pamphlet.data.model.WorkModel;
import com.common.httplinker.*;

public class Controller implements Resourcer {
	public static Resourcer instance(Context context){
		return new Controller(context);
	}
	
	private Context context;
	
	private Controller(Context context){
		this.context = context.getApplicationContext();
	}
	@Override
	public List<CircleModel> getAllList() {
		return DataBase.instance(context).getAllCircles();
	}
	@Override
	public List<CircleModel> getFavouriteList() {
		return DataBase.instance(context).getFavourCircles();
	}
	@Override
	public List<WorkModel> getSearchResult(String search) {
		return DataBase.instance(context).search(search);
	}

	@Override
	public void update(final Handler handler) {
		handler.sendEmptyMessage(0);
		new Thread(){
			@Override
			public void run() {
				try {
					UpdateResult checkResult = checkUpdate();
					if(checkResult.getUpdateCode().equals(Data.instance(context).getUpdateCode())) {
						handler.sendEmptyMessage(5);
						return;
					}
					handler.sendEmptyMessage(1);
					HttpLinkClient client = new HttpLinkClient();
					String result = client.synGet(Data.instance(context).getDataUrl(), null);
					handler.sendEmptyMessage(2);
					DataBase.instance(context.getApplicationContext()).updateData(result);
					handler.sendEmptyMessage(3);
					Data.instance(context).setUpdated(checkResult.getUpdateCode());
				} catch (Exception e) {
					handler.sendEmptyMessage(4);
				}

			}
		}.start();
	}

	@Override
	public void sendResponse(String s,Handler handler) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "发送成功"+s, Toast.LENGTH_LONG).show();
	}

	@Override
	public void initial() {
		DataBase.instance(context).initialData();
	}
	
	@Override
	public void checkUpdate(final Handler handler) {
		new Thread(){
			@Override
			public void run() {
				UpdateResult result = checkUpdate();
				if(result == null) return;
				boolean isUpdate = !result.getUpdateCode().equals(Data.instance(context).getUpdateCode());
				if(handler!=null && isUpdate)handler.sendEmptyMessage(6);
				//TODO check APP update
			}
		}.start();
	}
	
	private UpdateResult checkUpdate(){
		HttpLinkClient client = new HttpLinkClient();
		Data.instance(context);
		String result = client.synGet(Data.CHECK_URL, null);
		return Data.instance(context).refresh(result);
	}
	@Override
	public CircleModel getCircle(int cid) {
		DataBase.instance(context).getCircle(cid);
		return null;
	}
}
