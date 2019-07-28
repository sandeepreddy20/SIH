package com.example.android.raghu;

public class Data {

    private String title;
    private String description;
    private String image;
   // private String likes;
    public Data()
    {

    }

    public Data(String locationtxt, String imageref,String descriptiontxt) {
        this.title = locationtxt;
        this.description = descriptiontxt;
        this.image = imageref;
      //  this.likes = lik;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

  /*  public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }*/
}
