/**
  * Copyright 2020 bejson.com 
  */
package com.example.docker_android.Entity.Container;
import java.util.List;

/**
 * Auto-generated: 2020-02-14 14:23:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Container {

    private String Id;
    private List<String> Names;
    private String Image;
    private String ImageID;
    private String Command;
    private long Created;
    private List<String> Ports;
    private Labels Labels;
    private String State;
    private String Status;
    private HostConfig HostConfig;
    private NetworkSettings NetworkSettings;
    private List<String> Mounts;
    public void setId(String Id) {
         this.Id = Id;
     }
     public String getId() {
         return Id;
     }

    public void setNames(List<String> Names) {
         this.Names = Names;
     }
     public List<String> getNames() {
         return Names;
     }

    public void setImage(String Image) {
         this.Image = Image;
     }
     public String getImage() {
         return Image;
     }

    public void setImageID(String ImageID) {
         this.ImageID = ImageID;
     }
     public String getImageID() {
         return ImageID;
     }

    public void setCommand(String Command) {
         this.Command = Command;
     }
     public String getCommand() {
         return Command;
     }

    public void setCreated(long Created) {
         this.Created = Created;
     }
     public long getCreated() {
         return Created;
     }

    public void setPorts(List<String> Ports) {
         this.Ports = Ports;
     }
     public List<String> getPorts() {
         return Ports;
     }

    public void setLabels(Labels Labels) {
         this.Labels = Labels;
     }
     public Labels getLabels() {
         return Labels;
     }

    public void setState(String State) {
         this.State = State;
     }
     public String getState() {
         return State;
     }

    public void setStatus(String Status) {
         this.Status = Status;
     }
     public String getStatus() {
         return Status;
     }

    public void setHostConfig(HostConfig HostConfig) {
         this.HostConfig = HostConfig;
     }
     public HostConfig getHostConfig() {
         return HostConfig;
     }

    public void setNetworkSettings(NetworkSettings NetworkSettings) {
         this.NetworkSettings = NetworkSettings;
     }
     public NetworkSettings getNetworkSettings() {
         return NetworkSettings;
     }

    public void setMounts(List<String> Mounts) {
         this.Mounts = Mounts;
     }
     public List<String> getMounts() {
         return Mounts;
     }

}