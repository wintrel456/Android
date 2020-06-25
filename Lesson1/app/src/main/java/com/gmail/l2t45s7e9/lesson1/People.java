package com.gmail.l2t45s7e9.lesson1;

import java.util.Comparator;
import java.util.GregorianCalendar;

public class People {
    private final String name;
    private final String telephoneNumber;
    private final  String telephoneNumber2;
    private final  String email;
    private final  String email2;
    private final  String description;
    private final  String birthDate;
    private  final  String id;


    People(String name, String telephoneNumber, String telephoneNumber2, String email, String email2, String description, String birthDate, String id  ){
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.telephoneNumber2 = telephoneNumber2;
        this.email2 = email2;
        this.description = description;
        this.birthDate=birthDate;
        this.id = id;
    }



    public String getName() {
        return name;
    }
    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public String getTelephoneNumber2() {
        return telephoneNumber2;
    }
    public String getEmail() {
        return email;
    }
    public String getEmail2() {
        return email2;
    }
    public String getDescription() {
        return description;
    }
    public String getBirthDate(){return birthDate;}
    public String getId(){return id;}


}