package com.onetoone.store;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id String email;
    private String name;
    private String Partneremail;
    private String sharedCode;

    private String uniqueCoupleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSharedCode() {
        return sharedCode;
    }

    public void setSharedCode( String sharedCode ) {
        this.sharedCode = sharedCode;
    }

    public String getPartneremail() {
        return Partneremail;
    }

    public void setPartneremail( String partneremail ) {
        Partneremail = partneremail;
    }


    public String getUniqueCoupleId() {
        return uniqueCoupleId;
    }

    public void setUniqueCoupleId( String uniqueCoupleId ) {
        this.uniqueCoupleId = uniqueCoupleId;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", name=" + name
                + "]";
    }

}