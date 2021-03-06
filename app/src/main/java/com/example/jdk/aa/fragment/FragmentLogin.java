package com.example.jdk.aa.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdk.aa.R;
import com.example.jdk.aa.utility.MyAuthCodeUtils;


import butterknife.Bind;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by JDK on 2016/7/28.
 */
public class FragmentLogin extends Fragment{
    @Bind(R.id.email_login_id)
    EditText email_login_et;
    @Bind(R.id.password_login_id)
    EditText password_login_et;
    @Bind(R.id.login_or_register)
    Button login_or_register_bt;
    @Bind(R.id.authcodeedittext_login_id)
    EditText authcode_login_et;
    @Bind(R.id.authcode_login_id)
    MyAuthCodeUtils authcode_myAuthCode_Utils;
    OnLoginbtClickListener myOnLoginbtClickListener;
    String  email_login_et_value;
    String  password_login_et_value;
    String  authcode_login_et_value;
    String  authcode_myAuthCode_Utils_value;
    boolean isAuthcode;
    public interface OnLoginbtClickListener{
        public void OnLoginbtClick(boolean isAuthcode,String email_login_et_value,String password_login_et_value);
    }
    public void setLoginbtListener(OnLoginbtClickListener myOnLoginbtClickListener){
        this.myOnLoginbtClickListener=myOnLoginbtClickListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAuthcode=false;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,v);
        return v;
    }
    @OnClick(R.id.login_or_register)
    public void onClick(View v){
        email_login_et_value=email_login_et.getText().toString();
        password_login_et_value=password_login_et.getText().toString();
        authcode_login_et_value=authcode_login_et.getText().toString();
        authcode_myAuthCode_Utils_value=authcode_myAuthCode_Utils.getmMyAuthcodeText().toString();
        if(authcode_login_et_value.contentEquals(authcode_myAuthCode_Utils_value)){
            isAuthcode=true;
        }
            if(myOnLoginbtClickListener!=null) {
                myOnLoginbtClickListener.OnLoginbtClick(isAuthcode,email_login_et_value, password_login_et_value);
            }
    }
}
