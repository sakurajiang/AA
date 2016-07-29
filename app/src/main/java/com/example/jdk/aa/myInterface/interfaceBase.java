package com.example.jdk.aa.myInterface;

import com.example.jdk.aa.model.ModelResultSet;

import java.util.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JDK on 2016/7/28.
 */
public interface interfaceBase {
    @FormUrlEncoded
    @POST("/LoginServer/login.php")
    rx.Observable<ModelResultSet> loginMainActivity(@Field("email") String email,@Field("password") String password);

    @FormUrlEncoded
    @POST("/LoginServer/register.php")
    rx.Observable<ModelResultSet> register(@Field("name") String name, @Field("email") String email, @Field("contact") String contact, @Field("password") String password);

}
