package com.topweather.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.topweather.app.db.TopWeatherOpenHelper;

public class TopWeatherDB {
	/***database's name***/
	
	public static final String DB_NAME = "top_weather";
	
	/***database's version***/
	public static final int VERSION = 1;
	private static TopWeatherDB topWeatherDB;
	private SQLiteDatabase db;
	
	/***privatize the structed function***/
	private TopWeatherDB(Context context){
		TopWeatherOpenHelper dbHelper = new TopWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
		
	}
	
	/***Obtain the TopWeatherDB's Living example***/
	
	public synchronized static TopWeatherDB getInstance(Context context){
		if(topWeatherDB == null){
			topWeatherDB = new TopWeatherDB(context);
		}
		return topWeatherDB;
	}
	/***Province living example store to database****/
	
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());	
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}	
	}
	
	/*** Read information of all provinces of China from databases***/
	
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
				
			}while(cursor.moveToNext());
			
		}
		return list;
		
	}
	
/***City living example store to database****/
	
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());	
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}	
	}
	
	/*** Read information of all cities of provinces from databases***/
	
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db
				.query("City", null, "province_id = ?", 
						new String[] {String.valueOf(provinceId) },null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
				
			}while(cursor.moveToNext());
			
		}
		return list;	
	}
	
/***Country living example store to database****/
	
	public void saveCountry(Country country){
		if(country != null){
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());	
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
		}	
	}
	
	/*** Read information of all countries of China from databases***/
	
	public List<Country> loadCountries(int cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db
				.query("Country", null, "city_id = ?",
						new String[] {String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cityId);
				list.add(country);
				
			}while(cursor.moveToNext());
			
		}
		return list;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}





