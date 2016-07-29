package com.example.jdk.aa.activity.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import com.example.jdk.aa.R;
import com.example.jdk.aa.fragment.FragmentLogin;
import com.example.jdk.aa.model.ModelResultSet;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by JDK on 2016/7/28.
 */
public class ActivityBaseOfLRActivity extends Activity{
    protected Subscription subscription;
    public void unSubscription(){
        if(subscription!=null&&subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
    //替换Fragment
    public void replaceAnFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.id_content,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
