package com.topweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.topweather.app.model.City;
import com.topweather.app.model.County;
import com.topweather.app.model.Province;
import com.topweather.app.model.TopWeatherDB;
import com.topweather.app.util.HttpCallbackListener;
import com.topweather.app.util.HttpUtil;

public class ChooseAreaActivity extends Activity{
	
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private TopWeatherDB topWeatherDB;
	private List<String> dataList = new ArrayList<String> ();
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.choose_area);
	    listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout
				.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		topWeatherDB = TopWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener()){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, 
					long arg3){
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(index);
					queryCities();
					}else if (currentLevel == LEVEL_CITY){
						selectedCity = cityList.get(index);
						queryCounties();
						}
			}
		});
		queryProvinces();
		
}

	//enquire all provinces in China, first to enquire from databases
	//if not in, and then to enquire in server
	
	private void queryProvinces(){
		provinceList = topWeatherDB.loadProvinces();
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province province : provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null, "province");
		}
	}
	
	//enquire all cities in Province selected, first to enquire from databases
		//if not in, and then to enquire in server
	

	private void queryCities(){
		cityList = topWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			for(City city: cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
	

	//enquire all counties in city selected, first to enquire from databases
			//if not in, and then to enquire in server
		
	private void queryCounties() {
	    countyList = topWeatherDB.loadCounties(selectedCity.getId());
	    if (countyList.size() > 0) {
	        dataList.clear();
	        for (County county : countyList) {
	            dataList.add(county.getCountyName());
	        }
	        adapter.notifyDataSetChanged();
	        listView.setSelection(0);
	        titleText.setText(selectedCity.getCityName());
	        currentLevel = LEVEL_COUNTY;
	    } else {
	        queryFromServer(selectedCity.getCityCode(), "county");
	        } 
	    }
	//enquire province, city, county data from server based the code & type you send to 
	
	private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
        		address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                	result = Utility.handleProvincesResponse(coolWeatherDB, response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(coolWeatherDB,
                           response, selectedProvince.getId());
                } else if ("county".equals(type)) {
                	result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
                }
                if (result) {
                	// 通过runOnUiThread()方法回到主线程处理逻辑 runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           closeProgressDialog();
                           if ("province".equals(type)) {
                               queryProvinces();
                           } else if ("city".equals(type)) {
                               queryCities();
                           } else if ("county".equals(type)) {
                               queryCounties();
                           }
                       }
                  });
            	}

        	}
        
        @Override
        public void onError(Exception e){
        	runOnUiThread(new Runnable(){
        		@Override
        		public void run(){
        			closeProgressDialog();
        			Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
        		}
        	});
        	
        }
      });
   }


private void showProgressDialog(){
	if(progressDialog == null){
		progressDialog  = new ProgressDialog(this);
		progressDialog.setMessage("正在加载...");
		progressDialog.setCanceledOnTouchOutside(false);
		
	}
	progressDialog.show();
}


//close the progress dialog

private void closeProgressDialog(){
	if(progressDialog != null){
		progressDialog.dismiss();
		}
}

      //  catch Back key, judge by present rank
		//return list of province or city ,or exit in direct

@Override 
public void onBackPressed(){
	if(currentLevel == LEVEL_COUNTY){
		queryCities();
	}else if(currentLevel == LEVEL_CITY){
		queryProvinces();
	}else{
		finish();
	}
}
}

        
        
        
        
        
        
