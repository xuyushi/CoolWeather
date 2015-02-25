package com.example.xuyushi.coolweather.util;

/**
 * Created by xuyushi on 15/2/25.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
