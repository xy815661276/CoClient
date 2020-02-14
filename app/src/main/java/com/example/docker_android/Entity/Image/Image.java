/**
  * Copyright 2020 bejson.com 
  */
package com.example.docker_android.Entity.Image;
import java.util.List;

/**
 * Auto-generated: 2020-02-14 18:39:42
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Image {

    private int Containers;
    private long Created;
    private String Id;
    private String Labels;
    private String ParentId;
    private List<String> RepoDigests;
    private List<String> RepoTags;
    private int SharedSize;
    private long Size;
    private long VirtualSize;

    public void setContainers(int Containers) {
        this.Containers = Containers;
    }

    public int getContainers() {
        return Containers;
    }

    public void setCreated(long Created) {
        this.Created = Created;
    }

    public long getCreated() {
        return Created;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public void setLabels(String Labels) {
        this.Labels = Labels;
    }

    public String getLabels() {
        return Labels;
    }

    public void setParentId(String ParentId) {
        this.ParentId = ParentId;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setRepoDigests(List<String> RepoDigests) {
        this.RepoDigests = RepoDigests;
    }

    public List<String> getRepoDigests() {
        return RepoDigests;
    }

    public void setRepoTags(List<String> RepoTags) {
        this.RepoTags = RepoTags;
    }

    public List<String> getRepoTags() {
        return RepoTags;
    }

    public void setSharedSize(int SharedSize) {
        this.SharedSize = SharedSize;
    }

    public int getSharedSize() {
        return SharedSize;
    }

    public void setSize(long Size) {
        this.Size = Size;
    }

    public long getSize() {
        return Size;
    }

    public void setVirtualSize(long VirtualSize) {
        this.VirtualSize = VirtualSize;
    }

    public long getVirtualSize() {
        return VirtualSize;
    }
}