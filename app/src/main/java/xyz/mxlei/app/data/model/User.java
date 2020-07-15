package xyz.mxlei.app.data.model;

import java.io.Serializable;

/**
 * @author mxlei
 * @date 2020/7/12
 */
public class User implements Serializable {

    private String name;
    private String password;
    private String phone;
    private int age;
    private int gender;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
