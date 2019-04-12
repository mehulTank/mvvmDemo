package com.cmexpertise.beautyapp.model.locationModel;

/**
 * Created by admin on 9/20/2016.
 */
public class LocationModel {
    String name;
    int id;


    public LocationModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public LocationModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
