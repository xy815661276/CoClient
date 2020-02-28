package com.example.docker_android.DockerAPI.Configuration;

import android.util.Log;

public class CreateContainerConfig {
    /**
     * 容器的一些常规配置
     * @param image 镜像
     * @return
     */
    public static String getJSON(String image,String host_port,String container_port,String host_volume,String container_volume) {
        String json =   "{\n" +
                "  \"Hostname\": \"\",\n" +
                "  \"Domainname\": \"\",\n" +
                "  \"AttachStdin\": true,\n" +    //-i参数
                "  \"AttachStdout\": true,\n" +
                "  \"AttachStderr\": true,\n" +
                "  \"Tty\": true," +              //-t参数
                "  \"Image\": \"IMAGE_TAG_REPLACE\",\n" +
                "   \"Cmd\": [\n" +
                "   \"/bin/bash\"\n" +
                "   ],\n" +
                "  \"ExposedPorts\": {\n" +
                "   \"CONTAINER_PORT/tcp\": {}\n" +
                "  },\n" +
                "  \"HostConfig\": {\n" +
                "     \"Binds\": [\n" +
                "      \"HOST_VOLUMES:CONTAINER_VOLUMES\"\n" +  //绑定
                "      ]," +
                "    \"PortBindings\": {\n" +
                "      \"CONTAINER_PORT/tcp\": [\n" +
                "        {\n" +
                "          \"HostIp\": \"0.0.0.0\",\"HostPort\": \"HOST_PORT_REPLACE\"\n" +
                "        }\n" +
                "       ]\n "+
                "   }\n" +
                "  }\n" +
                "}";
                json = json.replace("IMAGE_TAG_REPLACE", image)
                .replace("HOST_PORT_REPLACE", host_port)
                .replace("CONTAINER_PORT", container_port)
                .replace("HOST_VOLUMES", host_volume)
                .replace("CONTAINER_VOLUMES", container_volume);
        Log.d("Config",json);
        return json;
    }
}
