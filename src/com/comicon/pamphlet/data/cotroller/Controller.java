package com.comicon.pamphlet.data.cotroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
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
	public void sendResponse(String s,final Handler handler) {
		HttpLinkClient client = new HttpLinkClient();
		ArrayList<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("response",s));
		client.asyPost(Data.instance(context).getResponseUrl(),para, new HttpHandler() {
			@Override
			public void onSucceed(String result) {
				if(handler!=null) handler.sendEmptyMessage(0);
			}
		});
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
//				else if(handler!=null ) handler.sendEmptyMessage(5);
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
		return DataBase.instance(context).getCircle(cid);
	}
}
