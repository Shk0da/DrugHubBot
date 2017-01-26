package org.telegram.drughubbot.database.model;


public class User {

    private String name;
    private long userId;
    private String location;
    private String city;
    private String phone;
    private int isDealer = 0;
    private String lang = "ru";
    private int rating;
    private int isBlock;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(int isDealer) {
        this.isDealer = isDealer;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", isDealer=" + isDealer +
                ", lang='" + lang + '\'' +
                ", rating=" + rating +
                ", isBlock=" + isBlock +
                '}';
    }
}
