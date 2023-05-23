package com.example.palliativecareapppatients;

public class Contacts {

    public String firstName,middleName ,familyName,status,image;

    public Contacts()
    {

    }

    public Contacts(String firstName,String middleName ,String familyName,String status, String image) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.familyName = familyName;

        this.status = status;
        this.image = image;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setSFirstName(String FirstName) {
        this.firstName = FirstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String MiddleName) {
        this.middleName = MiddleName;
    }
    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String FamilyName) {
        this.familyName = FamilyName;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
