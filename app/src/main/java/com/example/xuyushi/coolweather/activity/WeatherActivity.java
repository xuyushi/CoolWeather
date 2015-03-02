package com.example.xuyushi.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xuyushi.coolweather.R;
import com.example.xuyushi.coolweather.util.HttpCallbackListener;
import com.example.xuyushi.coolweather.util.HttpUtil;
import com.example.xuyushi.coolweather.util.Utility;

import static com.example.xuyushi.coolweather.R.id.publish_text;

public class WeatherActivity extends Activity {

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;      //城市名称
    private TextView publishText;       //发布时间
    private TextView weatherDespText;   //天气描述信息
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDataText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        currentDataText = (TextView) findViewById(R.id.curent_data);
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            //有县级代码代号就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);

        } else {
            //没有县级代码时就直接显示本地天气
            showWeather();
        }
    }


    //查询县级所对应的天气代号
    private void queryWeatherCode(String countyCode) {

        String address = "http://m.weather.com.cn/data5/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    //查询天气代号所对应的天气http://m.weather.com.cn/data
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://m.weather.com.cn/data/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");

    }

    //根据传入的地址和类型去服务器查询天气代号或者天气信息

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        //从服务器解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weaherCode = array[1];
                            queryWeatherInfo(weaherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    //处理返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });

            }
        });
    }


    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
      //  temp2Text.setText(prefs.getString("temp2", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(prefs.getString("今天" + prefs.getString("publish_time", "") + "发布", ""));
        currentDataText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
