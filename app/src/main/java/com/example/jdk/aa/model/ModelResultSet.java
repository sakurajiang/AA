package com.example.jdk.aa.model;

/**
 * Created by JDK on 2016/7/28.
 */
public class ModelResultSet {
    private boolean success;
    private String tag;
    private MyResultSet myResultSet;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public MyResultSet getMyResultSet() {
        return myResultSet;
    }

    public void setMyResultSet(MyResultSet myResultSet) {
        this.myResultSet = myResultSet;
    }

    public static class MyResultSet{
        String name;
        String email;
        String contact;

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

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
    }
}
