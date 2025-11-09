package com.anubhab09.demo_project1;

// Just for an endpoint testing 
public class SoftwareEngineer {
    private int id;
    private String name;
    private String techstack;

    public SoftwareEngineer(int id, String name, String techstack){
        this.id=id;
        this.name=name;
        this.techstack=techstack;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechstack() {
        return techstack;
    }

    public void setTechstack(String techstack) {
        this.techstack = techstack;
    }
}
