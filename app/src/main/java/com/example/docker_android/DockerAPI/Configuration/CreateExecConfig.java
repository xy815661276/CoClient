package com.example.docker_android.DockerAPI.Configuration;

import android.util.Log;

import java.util.Arrays;

public class CreateExecConfig {
    /**
     * 创建EXEC的配置
     * @param command 镜像
     * @return
     */
    public static String getJSON(String command,String working_dir) {
        String result = "";
        for (String cmd:command.split(" ")){
            result = result + "\"" + cmd + "\",\n";
        }
        String json =
                "{\n" +
                        "\"AttachStdin\": false,\n" +
                        "\"AttachStdout\": true,\n" +
                        "\"AttachStderr\": true,\n" +
                        "\"DetachKeys\": \"ctrl-p,ctrl-q\",\n" +
                        "\"Tty\": false,\n" +
                        "\"Cmd\": [\n" +
                        result +
                        "],\n" +
                        "\"WorkingDir\": \"WORKING_DIR\"\n" +
                        "}";
        json = json.replace(",\n]","\n]");
        json = json.replace("WORKING_DIR",working_dir);
        Log.d("ExecConfig",json);
        return json;
    }
//
//    public static void main(String[] args) {
//        getJSON("sudo kextcache -i /");
//    }
}
