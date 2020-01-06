package com.example.docker_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.docker_android.DockerUtils.DockerClientService;
import com.github.dockerjava.api.DockerClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DockerClientService dockerClientService =new DockerClientService();
        //连接docker服务器
        DockerClient client = dockerClientService.connectDocker();
    }
}
