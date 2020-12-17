package com.example.docker_android.DockerAPI;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MigrateService {

    /**
     * 发起容器迁移请求
     * @param callback 回调方法
     */
    public static void container_migrate(String url,String container_id,String checkpoint_name,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url+"/migrate/container?"+"container_id="+container_id+"&checkpoint_name="+checkpoint_name)
                .build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * 发起镜像迁移请求
     * @param callback 回调方法
     */
    public static void image_migrate(String url,String container_id,String repository,String tag,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url+"/migrate/container?"+"container_id="+container_id+"&repository="+repository+"&tag="+tag)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
