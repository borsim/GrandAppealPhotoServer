package com.shaunthegoat.spring.rest;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date uploadDate;
    private int horizontalImageLayout;
    private int verified;
    private long uploaderAppid;
    private String imageLocation;
    private String imageFileName;

      public void setId(long id) {
        this.id = id;
    }
	
    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getHorizontalImageLayout() {
        return horizontalImageLayout;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getImageLocation() {
        return imageLocation;
    }
    
    public void setUploaderAppid(long uploaderAppid) {
	this.uploaderAppid = uploaderAppid;
    }

    public long getUploaderAppid() {
	return uploaderAppid;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }   

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
     
    
    
}
