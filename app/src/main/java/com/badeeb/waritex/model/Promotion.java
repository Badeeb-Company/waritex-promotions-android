package com.badeeb.waritex.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 6/7/2017.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Promotion {

    // Class Attributes
    @Expose(serialize = false, deserialize = true)
    @SerializedName("id")
    private int id;

    @Expose(serialize = false, deserialize = true)
    @SerializedName("title")
    private String title;

    @Expose(serialize = false, deserialize = true)
    @SerializedName("description")
    private String description;

    @Expose(serialize = false, deserialize = true)
    @SerializedName("due_date")
    private String dueDate;

    @Expose(serialize = false, deserialize = true)
    @SerializedName("main_photo")
    private String mainPhoto;

    @Expose(serialize = false, deserialize = true)
    @SerializedName("photos")
    private List<PromotionPhoto> photos;

    public static final String PROMOTION_TAG_LOG = "Promotion_Object";

    // Default constructor
    public Promotion() {
        this.id = -1;
        this.title = "";
        this.description = "";
        this.dueDate = "";
        this.mainPhoto = "";
        this.photos = new ArrayList<>();
    }

    // Setters and Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public List<PromotionPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PromotionPhoto> photos) {
        this.photos = photos;
    }

    public String getDueDateFormated(){
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DISPLAY_PATTERN);
        return dateFormat.format(getDueDate());
        */
        return "";
    }

    public void setDueDateFormated(String dueDateText){
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WRITE_PATTERN);
        try {
            setDueDate(dateFormat.parse(dueDateText));
        } catch (ParseException e) {
            Log.d(PROMOTION_TAG_LOG, dueDateText+" - "+ e.getMessage());
        }
        */
    }


}
