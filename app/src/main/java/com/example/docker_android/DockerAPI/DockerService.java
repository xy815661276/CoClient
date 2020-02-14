package com.example.docker_android.DockerAPI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 容器服务类
 */
public class DockerService {
    private static String address = "http://121.36.19.122:2375";
    /**
     * 获取容器列表，GET方法
     * @param callback 回调方法
     */
    public static void getContainers(okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/containers/json?all=true")
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 获取一个容器的信息，GET方法
     * @param id 容器id
     * @param callback 回调方法
     */
    public static void getContainer(String id,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/containers/"+id+"/json")
                .build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * /containers/create
     */
    public static void createContainer(String id,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/containers/"+id+"/json")

                .build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * 获取镜像列表，GET方法
     * @param callback 回调方法
     */
    public static void getImages(okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/images/json")
                .build();
        client.newCall(request).enqueue(callback);
    }
}
