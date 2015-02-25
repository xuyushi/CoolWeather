package com.example.xuyushi.coolweather.util;

import android.text.TextUtils;

import com.example.xuyushi.coolweather.model.City;
import com.example.xuyushi.coolweather.model.CoolWeatherDB;
import com.example.xuyushi.coolweather.model.County;
import com.example.xuyushi.coolweather.model.Province;

/**
 * Created by xuyushi on 15/2/25.
 */
public class Utility {
    //handle the Province message from server
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,
                                                              String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }



    //handle the City message from server
    public synchronized static boolean handlevCityResponse(CoolWeatherDB coolWeatherDB,
                                                              String response,
                                                              int prvinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCitys = response.split(",");
            if (allCitys != null && allCitys.length > 0) {
                for (String p : allCitys) {
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(prvinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }




    //handle the County message from server
    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,
                                                              String response,
                                                              int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountys = response.split(",");
            if (allCountys != null && allCountys.length > 0) {
                for (String p : allCountys) {
                    String[] array = p.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
