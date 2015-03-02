package com.example.xuyushi.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.example.xuyushi.coolweather.model.City;
import com.example.xuyushi.coolweather.model.CoolWeatherDB;
import com.example.xuyushi.coolweather.model.County;
import com.example.xuyushi.coolweather.model.Province;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
 //           String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather1");
            String publishTime = weatherInfo.getString("date_y");
//            String publishTime = "test";
            savaWeatherInfo(context, cityName, weatherCode, temp1, weatherDesp, publishTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将所有的信息存储到SharedPreference中

    private static void savaWeatherInfo(Context context, String cityName, String weatherCode,
                                        String temp1,
                                        String weatherDesp, String publishTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日,", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
//        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_data", sdf.format(new Date(0)));

        editor.commit();
    }
}
