package com.example.docker_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.docker_android.DockerUtils.DockerClientService;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DockerClientService dockerClientService = null;
        try {
            dockerClientService = new DockerClientService();
            dockerClientService.getDockerInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
