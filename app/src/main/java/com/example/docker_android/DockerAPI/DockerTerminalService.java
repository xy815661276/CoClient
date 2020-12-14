package com.example.docker_android.DockerAPI;

import com.example.docker_android.Utils.RootCmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DockerTerminalService {
    /**
     * 通过终端命令执行docker ps
     *
     * @return 终端输出结果
     */
    public static String DockerPS() {
        String res = "";
        res = RootCmd.execRootCmd("docker ps");
        return res;
    }

    /**
     * 通过终端命令执行docker ps -a
     *
     * @return 终端输出结果
     */
    public static String DockerPSA() {
        String res = "";
        res = RootCmd.execRootCmd("docker ps -a");
        return res;
    }

    /**
     * 通过命令行创建checkpoint
     *
     * @param containerId    容器id
     * @param CheckpointName 检查点名称
     * @return 返回checkpoint的名称
     */
    public static String DockerCheckpoint(String containerId, String CheckpointName) {
        String res = "";
        res = RootCmd.execRootCmd("docker checkpoint create " + containerId + " " + CheckpointName);
        return res;
    }

    /**
     * 查找容器拥有的checkpoint
     * @param containerId 容器id
     * @return 返回checkpoint列表
     */
    public static List<String> GetCheckpoint(String containerId) {
        String res = "";
        res = RootCmd.execRootCmd("docker checkpoint ls " + containerId);
        String[] tmp = res.split("\n");
        List<String> out = new ArrayList<>();
        Collections.addAll(out, tmp);
        out.remove(0);
        return out;
    }
    /**
     * 通过命令行删除checkpoint
     *
     * @param containerId    容器id
     * @param CheckpointName 检查点名称
     * @return 返回checkpoint的名称
     */
    public static String DeleteCheckpoint(String containerId, String CheckpointName) {
        String res = "";
        res = RootCmd.execRootCmd("docker checkpoint rm " + containerId + " " + CheckpointName);
        return res;
    }
    /**
     * 通过命令行从checkpoint启动容器
     *
     * @param containerId    容器id
     * @param CheckpointName 检查点名称
     * @return 返回为空
     */
    public static String StartCheckpoint(String containerId, String CheckpointName) {
        String res = "";
        RootCmd.execRootCmd("docker stop " + containerId);
        res = RootCmd.execRootCmd("docker start --checkpoint " + CheckpointName + " " + containerId);
        return res;
    }
}
