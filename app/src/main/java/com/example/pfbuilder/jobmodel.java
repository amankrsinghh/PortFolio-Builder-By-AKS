package com.example.pfbuilder;

public class jobmodel {
    public String getJobicon() {
        return jobicon;
    }

    public void setJobicon(String jobicon) {
        this.jobicon = jobicon;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public jobmodel(String jobicon, String jobtitle, String company, String location, String description, String salary) {
        this.jobicon = jobicon;
        this.jobtitle = jobtitle;
        this.company = company;
        this.location = location;
        this.description = description;
        this.salary = salary;
    }

    String jobicon ,jobtitle, company, location, description, salary;
}
