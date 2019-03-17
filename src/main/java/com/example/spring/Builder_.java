package com.example.spring;

/**
 * 建造者模式
 * @Author: Lee
 * @Date: 2019/03/14 14:55
 */
public class Builder_ {
}

class Programmer {
    private String firstName;
    private String lastName;
    private String address;
    private String zipCode;
    private String city;
    private String[] languages;
    private String[] projects;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }
}

class ProgrammerBuilder {
    private Programmer programmer;

    public ProgrammerBuilder setFirstName(String firstName) {
        this.programmer.setFirstName(firstName);
        return this;
    }

    public ProgrammerBuilder setLastName(String lastName) {
        this.programmer.setLastName(lastName);
        return this;
    }

    public ProgrammerBuilder setAddress(String address) {
        this.programmer.setAddress(address);
        return this;
    }

    public ProgrammerBuilder setZipCode(String zipCode) {
        this.programmer.setZipCode(zipCode);
        return this;
    }

    public ProgrammerBuilder setCity(String city) {
        this.programmer.setCity(city);
        return this;
    }

    public ProgrammerBuilder setLanguages(String[] languages) {
        this.programmer.setLanguages(languages);
        return this;
    }

    public ProgrammerBuilder setProjects(String[] projects) {
        this.programmer.setProjects(projects);
        return this;
    }

}