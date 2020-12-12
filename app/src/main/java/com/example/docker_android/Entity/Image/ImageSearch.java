
package com.example.docker_android.Entity.Image;
import java.util.List;

public class ImageSearch {

    private int star_count;
    private boolean is_official;
    private boolean is_automated;
    private String name;
    private String description;

    public int getStar_count() {
        return star_count;
    }

    public void setStar_count(int star_count) {
        this.star_count = star_count;
    }

    public boolean is_official() {
        return is_official;
    }

    public void setIs_official(boolean is_official) {
        this.is_official = is_official;
    }

    public boolean is_automated() {
        return is_automated;
    }

    public void setIs_automated(boolean is_automated) {
        this.is_automated = is_automated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}