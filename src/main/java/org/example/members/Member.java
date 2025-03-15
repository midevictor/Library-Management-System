package org.example.members;

public class Member {
    private int member_id;
    private String name;
    private String email;
    private String phone;

    public  Member() {

    }


    // CONSTRUCTOR
    public Member( int member_id, String name, String email, String phone) {
        this.member_id = member_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public int getMemberId() {
        return member_id;
    }

    public void setMemberId(int member_id) {
        this.member_id = member_id;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
