package com.example.jdk.aa.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jdk.aa.R;
import com.example.jdk.aa.activity.base.ActivityBaseOfLRActivity;
import com.example.jdk.aa.fragment.FragmentLogin;
import com.example.jdk.aa.fragment.FragmentRegister;
import com.example.jdk.aa.model.ModelResultSet;
import com.example.jdk.aa.utility.HttpUtils;

import rx.Subscriber;

public class ActivityLoginOrRegister extends ActivityBaseOfLRActivity implements FragmentLogin.OnLoginbtClickListener,FragmentRegister.OnRegisterbtClickListener{
    HttpUtils httpUtils;
    FragmentLogin fragmentLogin;
    FragmentRegister fragmentRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);
        setDefaultFragment();
        fragmentLogin.setLoginbtListener(this);
        httpUtils=new HttpUtils();
    }
    public void setDefaultFragment(){
        fragmentLogin=new FragmentLogin();
        replaceAnFragment(fragmentLogin);
    }
    //创建一个观察者，这个观察者是登陆和注册共用的
    public Subscriber<ModelResultSet> createLoginSubscriber(){
        Subscriber<ModelResultSet> subscriber=new Subscriber<ModelResultSet>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ActivityLoginOrRegister.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ModelResultSet modelResultSet) {
                if(modelResultSet.isSuccess()){
                    startActivity(new Intent(ActivityLoginOrRegister.this, ActivityMain.class));
                }
            }
        };
        return subscriber;
    }
    @Override
    public void OnLoginbtClick(boolean isAuthcode,String email_login_et_value, String password_login_et_value) {
        if(email_login_et_value.length()==0||password_login_et_value.isEmpty()){
                fragmentRegister = new FragmentRegister();
                replaceAnFragment(fragmentRegister);
                fragmentRegister.setLoginbtListener(this);
        }else if(isAuthcode){
            subscription=httpUtils.loginOrRegisterUtils(createLoginSubscriber(),email_login_et_value,password_login_et_value,null,null);
        }else{
            Toast.makeText(this,"please enter correct authcode",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void OnRegisterbtClick(boolean isRegister,String fragment_email_value, String fragment_password_value, String fragment_name_value, String fragment_contact_value) {
        if(fragment_name_value.length()!=0||!fragment_contact_value.isEmpty()) {
            if(isRegister) {
                Subscriber<ModelResultSet> modelResultSetSubscriber = createLoginSubscriber();
                subscription = httpUtils.loginOrRegisterUtils(modelResultSetSubscriber, fragment_email_value, fragment_password_value, fragment_name_value, fragment_contact_value);
            }else{
                Toast.makeText(this,"please enter correct authcode",Toast.LENGTH_SHORT).show();
            }
        }else if(isRegister) {
            httpUtils.loginOrRegisterUtils(createLoginSubscriber(), fragment_email_value, fragment_password_value, null, null);
        }
    }


}
