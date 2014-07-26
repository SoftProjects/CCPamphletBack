package com.comicon.pamphlet.data.dataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.comicon.pamphlet.data.bean.Circle;
import com.comicon.pamphlet.data.bean.Work;
import com.comicon.pamphlet.data.model.CircleModel;
import com.comicon.pamphlet.data.model.WorkModel;
import com.common.database.AbstractDataBase;
import com.common.database.DbTable;

public class DataBase extends AbstractDataBase {
	public final DbTable workTable = new WorkTable();
	public final DbTable circleTable = new CircleTable();
	public final DbTable favorTable = new FavorTable();
	private final DbTable[] tableInfos = new DbTable[]{
			workTable,
			circleTable,
			favorTable
	};
	private final static int version = 5;
	private final static String name = "data.db";
	
	private static DataBase db = null;
	public static DataBase instance(Context context){
		if(db == null && context == null) return null;
		if(db == null) db = new DataBase(context);
		return db;
	}
	
	private DataBase(Context context) {
		super(context, name, version);
		setTable(tableInfos);
	}
	private SparseArray<CircleModel> circleCache = new SparseArray<CircleModel>();
	
	public void updateData(String json) throws JSONException{
		SQLiteDatabase db = getWritableDatabase();
		
		JSONObject object = new JSONObject(json.trim().replace("&nbsp;", " "));

		db.delete(workTable.TABLE_NAME, null, null);
		db.delete(circleTable.TABLE_NAME, null, null);
		
		JSONArray works = object.getJSONArray("works");
		for(int i=0;i<works.length();i++){
			JSONObject work = works.getJSONObject(i);
			ContentValues values = new ContentValues();
			values.put("wid", work.getInt("wid"));
			values.put("cid", work.getInt("cid"));
			values.put("name", work.getString("name"));
			values.put("mode", work.getString("mode"));
			values.put("category", work.getString("category"));
			values.put("theme", work.getString("theme"));
			values.put("price", work.getString("price"));
			values.put("sample", work.getString("sample"));
			db.insert(workTable.TABLE_NAME, "wid", values );
		}
		
		JSONArray circles = object.getJSONArray("circles");
		for(int i=0;i<circles.length();i++){
			JSONObject circle = circles.getJSONObject(i);
			ContentValues values = new ContentValues();
			values.put("cid", circle.getInt("cid"));
			values.put("name", circle.getString("name"));
			values.put("mode", circle.getString("mode"));
			values.put("site", circle.getString("site"));
			values.put("property", circle.getString("property"));
			values.put("boothnum", circle.getString("boothnum"));
			values.put("c_order", circle.getString("order"));
			db.insert(circleTable.TABLE_NAME, "cid", values );
		}
		db.close();
		initialData();
	}

	public void initialData(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(circleTable.TABLE_NAME, null, null, null, null, null, null);
		circleCache.clear();
		while(c.moveToNext()){
			int cid = c.getInt(c.getColumnIndex("cid"));
			String name = c.getString(c.getColumnIndex("name"));
			String mode = c.getString(c.getColumnIndex("mode"));
			String site = c.getString(c.getColumnIndex("site"));
			String property = c.getString(c.getColumnIndex("property"));
			String boothnum = c.getString(c.getColumnIndex("boothnum"));
			String c_order = c.getString(c.getColumnIndex("c_order"));
			Cursor c2 = db.query(favorTable.TABLE_NAME, null, "cid = ?", new String[]{cid+""}, null, null, null);
			boolean isfavour=false;
			if(c2.moveToNext()) isfavour = c2.getString(c2.getColumnIndex("isfavour")).equals("true");
			c2.close();
			circleCache.put(cid, new Circle(cid, name, mode, site, property, boothnum, c_order, isfavour));
		}
	}
	
	public List<CircleModel> getAllCircles(){
		List<CircleModel> ret = new ArrayList<CircleModel>();
		for(int i=0;i<circleCache.size();i++){
			CircleModel circle = circleCache.valueAt(i);
			ret.add(circle);
		}
		return ret;
	}
	
	public List<CircleModel> getFavourCircles(){
		List<CircleModel> ret = new ArrayList<CircleModel>();
		for(int i=0;i<circleCache.size();i++){
			CircleModel circle = circleCache.valueAt(i);
			if(circle.isFavorite())ret.add(circle);
		}
		return ret;
	}	
	
	
	public List<WorkModel> search(String search){
		List<WorkModel> ret = new ArrayList<WorkModel>();
		SQLiteDatabase db = getWritableDatabase();
		SparseArray<CircleModel> circles = new SparseArray<CircleModel>();
		//TODO
		Cursor c = db.query(workTable.TABLE_NAME, null, "name like ?", new String[]{"%"+search+"%"}, null, null, null);
		while(c.moveToNext()){
			int wid = c.getInt(c.getColumnIndex("wid"));
			int cid = c.getInt(c.getColumnIndex("cid"));
			String name = c.getString(c.getColumnIndex("name"));
			String mode = c.getString(c.getColumnIndex("mode"));
			String category = c.getString(c.getColumnIndex("category"));
			String theme = c.getString(c.getColumnIndex("theme"));
			String price = c.getString(c.getColumnIndex("price"));
			String sample = c.getString(c.getColumnIndex("sample"));
			CircleModel circle = circles.get(cid);
			if (circle==null) {
				circle = getCircle(cid);
				circles.put(cid, circle);
			}
			ret.add(new Work(circle, wid, name, mode, category, theme, price, sample));
		}
		db.close();
		return ret;
	}
	
	public CircleModel getCircle(int cid){
		return circleCache.get(cid);
	}
	
	public void setFavourite(boolean isFavour,CircleModel cricle){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isfavour", isFavour+"");
		if(db.update(favorTable.TABLE_NAME, values , "cid=?", new String[]{cricle.getCid()+""})==0){
			values.put("cid", cricle.getCid()+"");
			db.insert(favorTable.TABLE_NAME, "cid", values);
		}
		db.close();
	}
	
	public List<WorkModel> getWorks(CircleModel circle){
		List<WorkModel> ret = new ArrayList<WorkModel>();
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(workTable.TABLE_NAME, null, "cid=?", new String[]{circle.getCid()+""}, null, null, null);
		while(c.moveToNext()){
			int wid = c.getInt(c.getColumnIndex("wid"));
			String name = c.getString(c.getColumnIndex("name"));
			String mode = c.getString(c.getColumnIndex("mode"));
			String category = c.getString(c.getColumnIndex("category"));
			String theme = c.getString(c.getColumnIndex("theme"));
			String price = c.getString(c.getColumnIndex("price"));
			String sample = c.getString(c.getColumnIndex("sample"));
			ret.add(new Work(circle, wid, name, mode, category, theme, price, sample));
		}
		db.close();
		return ret;
	}
}
