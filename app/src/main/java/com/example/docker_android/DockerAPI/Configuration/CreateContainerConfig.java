package com.example.docker_android.DockerAPI.Configuration;

import android.util.Log;

public class CreateContainerConfig {
    /**
     * 容器的一些常规配置
     * @param image 镜像
     * @return
     */
    public static String getJSON(String image,Integer host_port,Integer contianer_port) {
        String json =   "{\n" +
                "  \"Hostname\": \"\",\n" +
                "  \"Domainname\": \"\",\n" +
                "  \"Image\": \"IMAGE_TAG_REPLACE\",\n" +
                "   \"Cmd\": [\n" +
                "   \"/bin/bash\"\n" +
                "   ],\n" +
                "  \"Volumes\": {\n" +
                "   \"/volumes/data\": {\"/root/test\":\"/root/data\"}\n" +    //映射的对象组
                "  },\n" +
                "  \"ExposedPorts\": {\n" +
                "   \"CONTAINER_PORT/tcp\": {}\n" +
                "  },\n" +
                "  \"HostConfig\": {\n" +
                "    \"PortBindings\": {\n" +
                "      \"CONTAINER_PORT/tcp\": [\n" +
                "        {\n" +
                "          \"HostPort\": \"HOST_PORT_REPLACE\"\n" +
                "        }\n" +
                "       ]\n "+
                "   }\n" +
                "  }\n" +
                "}";
                json = json.replace("IMAGE_TAG_REPLACE", image)
                .replace("HOST_PORT_REPLACE", host_port.toString())
                .replace("CONTAINER_PORT", contianer_port.toString());
        Log.d("Config",json);
        return json;
    }
}
