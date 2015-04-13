package com.topweather.app.util;

import android.text.TextUtils;

import com.topweather.app.model.City;
import com.topweather.app.model.County;
import com.topweather.app.model.Province;
import com.topweather.app.model.TopWeatherDB;

public class Utility {
	/***analysis and handle the province data from server***/
	public synchronized static boolean handleProvincesResponse(TopWeatherDB
			topWeatherDB, String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for(String p : allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//make the data analysised from server store in Province-table
					topWeatherDB.saveProvince(province);	
	
			}
			return true;
			
		}
	}
		return false;
}
	
	/***analysis and handle the city data from server***/
	public static boolean handleCitiesResponse(TopWeatherDB
			topWeatherDB, String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//make the data analysised from server store in City-table
					topWeatherDB.saveCity(city);	
	
			}
			return true;
			
		}
	}
		return false;
}
	

	/***analysis and handle the county data from server***/
	public static boolean handleCountiesResponse(TopWeatherDB
			topWeatherDB, String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for(String c : allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//make the data analysised from server store in County-table
					topWeatherDB.saveCounty(county);	
	
			}
			return true;
			
		}
	}
		return false;
	}
	
}

