package com.example.jdk.aa.utility;

import android.util.Log;

import com.example.jdk.aa.model.ModelResultSet;
import com.example.jdk.aa.myInterface.interfaceBase;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JDK on 2016/7/28.
 */
public class HttpUtils {
    private final String BASEURL="http://192.168.56.1";
    private Subscription mySubscription;
    //创建reprofit对象
    private Retrofit getRetrofit(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }
    public Subscription loginOrRegisterUtils(Subscriber<ModelResultSet> subscriber,String email,String password,String name,String contact){
        //调用retrofir的onCreate()方法得到这个接口
        interfaceBase myInterfaceBase =getRetrofit().create(interfaceBase.class);
        //为了将登陆注册写在一个函数里，我也是够拼的，通过判断name和contact是否为空来判断是注册还是登陆
        if(name==null||contact==null) {
            mySubscription = myInterfaceBase.loginMainActivity(email.trim(), password.trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }else{
            mySubscription=myInterfaceBase.register(name.trim(),email.trim(),contact.trim(),password.trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
        return mySubscription;
    }

}
