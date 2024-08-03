package com.example.campusexpensemanager.Account;

public class Account  {
    public int Id;

    public String fullname;

    public String studentid;

    public String email;

    public String password;



    public void setId(int id) {
        this.Id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setStudentId(String studentid) {
        this.studentid = studentid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public int getId() {
        return Id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
