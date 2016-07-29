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

import com.example.jdk.aa.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JDK on 2016/7/28.
 */
public class FragmentRegister extends Fragment implements View.OnClickListener{
        EditText fragment_email;
        EditText fragment_password;
        EditText fragment_name;
        EditText fragment_contact;
        Button fragment_bt;
    OnRegisterbtClickListener myOnRegisterbtClickListener;
    String fragment_email_value,fragment_password_value,fragment_name_value,fragment_contact_value;

    @Override
    public void onClick(View v) {
        fragment_email_value=fragment_email.getText().toString();
        fragment_password_value=fragment_password.getText().toString();
        fragment_name_value=fragment_name.getText().toString();
        fragment_contact_value=fragment_contact.getText().toString();
        if(fragment_contact_value!=null&&fragment_name_value!=null&&fragment_password_value!=null&&fragment_email_value!=null){
            if(myOnRegisterbtClickListener!=null) {
                myOnRegisterbtClickListener.OnRegisterbtClick(fragment_email_value,fragment_password_value,fragment_name_value,fragment_contact_value);
            }
        }
    }
    //设置接口，回调
    public interface OnRegisterbtClickListener{
        public void OnRegisterbtClick(String fragment_email_value,String fragment_password_value,String fragment_name_value,String fragment_contact_value);
    }
    public void setLoginbtListener(OnRegisterbtClickListener myOnRegisterbtClickListener){
        this.myOnRegisterbtClickListener=myOnRegisterbtClickListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_register,container,false);
        fragment_email= (EditText) v.findViewById(R.id.fragment_email_register_id);
        fragment_password= (EditText) v.findViewById(R.id.fragment_password_register_id);
        fragment_name= (EditText) v.findViewById(R.id.fragment_name_register_id);
        fragment_contact= (EditText) v.findViewById(R.id.fragment_contact_register_id);
        fragment_bt= (Button) v.findViewById(R.id.fragment_register_and_login);
        fragment_bt.setOnClickListener(this);
        return v;
    }

}
