package com.example.pfbuilder;

public class pfmodel {
    private int id;
    private String name, jobtitle, location, about, skills, email, phoneno, image;

    // Constructor with ID (for fetching data from DB)
    public pfmodel(int id, String name, String jobtitle, String location, String about, String skills, String email, String phoneno, String image) {
        this.id = id;
        this.name = name;
        this.jobtitle = jobtitle;
        this.location = location;
        this.about = about;
        this.skills = skills;
        this.email = email;
        this.phoneno = phoneno;
        this.image = image;
    }

    // Constructor without ID (for inserting new users)
    public pfmodel(String name, String jobtitle, String location, String about, String skills, String email, String phoneno, String image) {
        this.name = name;
        this.jobtitle = jobtitle;
        this.location = location;
        this.about = about;
        this.skills = skills;
        this.email = email;
        this.phoneno = phoneno;
        this.image = image;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getJobtitle() { return jobtitle; }
    public String getLocation() { return location; }
    public String getAbout() { return about; }
    public String getSkills() { return skills; }
    public String getEmail() { return email; }
    public String getPhoneno() { return phoneno; }
    public String getImage() { return image; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setJobtitle(String jobtitle) { this.jobtitle = jobtitle; }
    public void setLocation(String location) { this.location = location; }
    public void setAbout(String about) { this.about = about; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneno(String phoneno) { this.phoneno = phoneno; }
    public void setImage(String image) { this.image = image; }
}
