package com.cmexpertise.beautyapp.model.categoryModel;

/**
 * Created by admin on 8/11/2016.
 */

public class Categories {

    private String id;
    private int ImageID;
    private String name;
    private String isCheck;

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }


    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The userId
     */
    public int getImageID() {
        return ImageID;
    }

    /**
     * @param userId The user_id
     */
    public void setImageID(int userId) {
        this.ImageID = userId;
    }


    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }


}


