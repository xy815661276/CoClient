
package com.example.docker_android.Entity.Container;

public class Labels {
    private String labels;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Labels{" +
                "labels='" + labels + '\'' +
                '}';
    }
}