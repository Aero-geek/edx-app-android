package org.edx.mobile.view.common;

public class Users {
    private String Email;
    private String courses;

    public Users(String email, String courses) {
        Email = email;
        this.courses = courses;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }
}
