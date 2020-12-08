package com.example.docker_android.DockerAPI;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.DockerAPI.Configuration.CreateContainerConfig;
import com.example.docker_android.DockerAPI.Configuration.CreateExecConfig;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 容器服务类
 */
public class DockerService {
    private static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    //private static String address = "http://121.36.19.122:2375";
    private static final String address = "http://127.0.0.1:2375";
    /**
     * 获取系统信息
     * @param callback 回调方法
     */
    public static void getInfo(okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/info")
                .build();
        client.newCall(request).enqueue(callback);
    }
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
     * 创建容器
     * @param name 容器名称
     * @param image 镜像名
     * @param host_port 主机端口
     * @param container_port 容器端口
     * @param host_volume 主机路径
     * @param container_volume 容器路径
     * @param callback 回调方法
     */
    public static void createContainer(String name,String image,String host_port,String container_port,String host_volume,String container_volume,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/containers/create?"+"name="+name)
                .post(RequestBody.create(JSON, CreateContainerConfig.getJSON(image,host_port,container_port,host_volume,container_volume)))
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

    /**
     * 获取容器的使用资源统计，GET方法
     * @param id 容器id
     * @param callback 回调方法
     */
    public static void getStats(String id,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/containers/"+id+"/stats?stream=false")
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 对容器进行的操作，启动，停止，重启等
     * @param id 容器id
     * @param action 操作方法
     * @param callback 回调方法
     */
    public static void ContainerAction(String id,String action,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request=new Request.Builder()
                .url(address+"/containers/"+id+"/"+action)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 删除容器
     * @param id 容器id
     * @param callback 回调方法
     */
    public static void DeleteContainer(String id,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("v","false")
                .build();
        Request request=new Request.Builder()
                .url(address+"/containers/"+id)
                .delete(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 拉取镜像
     * @param name 镜像名
     * @param tag 镜像标签
     * @param callback 回调方法
     */
    public static void CreateImage(String name,String tag,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)//设置超时时间为60s
                .readTimeout(60000, TimeUnit.MILLISECONDS).build();
        RequestBody body = new FormBody.Builder()
                .add("fromImage",name)
                .add("tag",tag)
                .build();
        Request request=new Request.Builder()
                .url(address+"/images/create")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * 删除镜像
     * @param id 镜像id
     * @param callback 回调方法
     */
    public static void DeleteImage(String id,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("force","false")
                .build();
        Request request=new Request.Builder()
                .url(address+"/images/"+id)
                .delete(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 创建exec
     * @param id 容器id
     * @param cmd 命令
     * @param working_dir 工作目录
     * @param callback 回调方法
     */
    public static void CreateExec(String id,String cmd,String working_dir,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/containers/"+id+"/exec")
                .post(RequestBody.create(JSON, CreateExecConfig.getJSON(cmd,working_dir)))
                .build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * 运行exec
     * @param id exec实例id
     * @param callback 回调方法
     */
    public static void StartExec(String id,okhttp3.Callback callback) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address+"/exec/"+id+"/start")
                .post(RequestBody.create(JSON,
                        "{\n" +
                        "\"Detach\": false,\n" +
                        "\"Tty\": false\n" +
                        "}"))
                .build();
        client.newCall(request).enqueue(callback);
    }

}
