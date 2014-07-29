package com.comicon.pamphlet.data.appsetting;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class Data {
	public static final String PREFERENCES_NAME = "AppData";
	private static final String DEFAULT_DATA_URL = "http://twzc.comicon1111.org/?c=api&a=getcdb&key=2ECBBB73";
//	public static final String DEFAULT_DATA_URL = "http://whitecomet.net/5066771dc042a8e4730680c5b91c7537.html";
	private static final String DEFAULT_MAP_URL = "http://bbs.comicon1111.org/pics/cc13/map2.jpg";
	private static final String DEFAULT_LOCALMAP_URL = "http://bbs.comicon1111.org/pics/cc12/CC12PLANO3.jpg";
	private static final String DEFAULT_UPDATE_CODE= "-1";
	private static final String DEFAULT_RESPONSE_URL="http://whitecomet.net/CCPamphlet.php/Index/response";
	
	public static final String IMAGE_CACHE = Environment.getExternalStorageDirectory().getPath()+"/ComiCon/";
	public static final String CHECK_URL = "http://whitecomet.net/CCPamphlet.php";

	private String dataUrl;
	private String mapUrl;
	private String localmapUrl;
	private String updateCode;
	private String responseUrl;
	
	private static Data instance = null;
	private Context context;
	public static Data instance(Context context){
		if(instance == null) instance = new Data(context.getApplicationContext());
		return instance;
	}
	public UpdateResult refresh(String json){
		try {
			JSONObject jobj = new JSONObject(json);
			SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
			Editor edit = pref.edit();
			edit.putString("dataUrl", jobj.getString("dataUrl"));
			edit.putString("mapUrl", jobj.getString("mapUrl"));
			edit.putString("localmapUrl", jobj.getString("localmapUrl"));
			edit.putString("responseUrl", jobj.getString("responseUrl"));
			edit.commit();
			//TODO
			instance = new Data(context);
			return new UpdateResult(jobj.getString("updateCode"), jobj.getString("appVersion"));
		} catch (Exception e) {}
		instance = new Data(context);
		return null;
	}
	
	public void setUpdated(String updateCode){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor edit = pref.edit();
		edit.putString("updateCode", updateCode);
		edit.commit();
		this.updateCode = updateCode;
	}
	
	private Data(Context context) {
		this.context = context;
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		dataUrl = pref.getString("dataUrl", DEFAULT_DATA_URL);
		mapUrl = pref.getString("mapUrl", DEFAULT_MAP_URL);
		localmapUrl = pref.getString("localmapUrl", DEFAULT_LOCALMAP_URL);
		updateCode = pref.getString("updateCode", DEFAULT_UPDATE_CODE);
		responseUrl = pref.getString("responseUrl", DEFAULT_RESPONSE_URL);
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public String getLocalmapUrl() {
		return localmapUrl;
	}

	public String getUpdateCode() {
		return updateCode;
	}
	
	public String getResponseUrl() {
		return responseUrl;
	}
}
