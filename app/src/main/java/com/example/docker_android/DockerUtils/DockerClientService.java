package com.example.docker_android.DockerUtils;

import com.alibaba.fastjson.JSONObject;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;

import java.util.List;


public class DockerClientService {
    public void getDockerInfo() throws InterruptedException, DockerException, DockerCertificateException {

        final DockerClient docker = DefaultDockerClient.fromEnv().build();
        // List all containers. Only running containers are shown by default.
        final List<Container> containers = docker.listContainers(DockerClient.ListContainersParam.allContainers());
        for(Container container:containers){
            System.out.println(container.names());
        }
        docker.close();
    }
}

