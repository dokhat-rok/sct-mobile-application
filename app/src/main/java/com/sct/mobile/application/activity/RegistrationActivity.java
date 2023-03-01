package com.sct.mobile.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.sct.mobile.application.R;
import com.sct.mobile.application.animation.EmptyInputAnimation;
import com.sct.mobile.application.component.observed.impl.RegistrationObservedImpl;
import com.sct.mobile.application.component.subscriber.RegistrationSubscriber;
import com.sct.mobile.application.model.dto.UserDto;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements RegistrationSubscriber {

    private RegistrationObservedImpl registrationObserved;

    private Toast toastReg;

    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        regButton = this.findViewById(R.id.reg_reg_button);
        regButton.setOnClickListener(this::onClickAccept);
        this.findViewById(R.id.reg_cancel_button).setOnClickListener(this::onClickCancel);
        toastReg = new Toast(this.getBaseContext());
        registrationObserved = new RegistrationObservedImpl();
        registrationObserved.subscribeRegistration(this);
    }

    public void onClickAccept(View view){
        regButton.setClickable(false);
        EditText loginEditText = Objects.requireNonNull(((TextInputLayout) this
                        .findViewById(R.id.reg_input_login))
                        .getEditText());
        EditText passEditText = Objects
                .requireNonNull(((TextInputLayout)this
                        .findViewById(R.id.reg_input_password))
                        .getEditText());
        EditText confPassEditText = Objects
                .requireNonNull(((TextInputLayout)this
                        .findViewById(R.id.reg_input_confirm_password))
                        .getEditText());

        boolean isNonEmptyData = true;

        if(loginEditText.getText().toString().equals("")){
            isNonEmptyData = false;
            EmptyInputAnimation.shake(loginEditText);
        }
        if(passEditText.getText().toString().equals("")){
            isNonEmptyData = false;
            EmptyInputAnimation.shake(passEditText);
        }
        if(confPassEditText.getText().toString().equals("")){
            isNonEmptyData = false;
            EmptyInputAnimation.shake(confPassEditText);
        }
        if(!passEditText.getText().toString().equals(confPassEditText.getText().toString())){
            isNonEmptyData = false;
            EmptyInputAnimation.shake(passEditText);
            EmptyInputAnimation.shake(confPassEditText);
        }

        if(!isNonEmptyData){
            regButton.setClickable(true);
            return;
        }

        registrationObserved.registration(loginEditText.getText().toString(),
                passEditText.getText().toString(),
                confPassEditText.getText().toString());
    }

    public void onClickCancel(View view){
        this.startActivity(new Intent(RegistrationActivity.this, AuthActivity.class));
    }

    @Override
    public void acceptRegistration(UserDto user) {
        regButton.setClickable(true);
        this.notification(user.getLogin() + ", регистрация выполнена успешна");
        this.startActivity(new Intent(RegistrationActivity.this, AuthActivity.class));
    }

    @Override
    public void errorRegistration(String error) {
        this.notification("Регистрация не получилась. Попробуйте ещё раз");
        regButton.setClickable(true);
    }

    private void notification(String text){
        toastReg.setText(text);
        toastReg.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registrationObserved.unSubscribeRegistration();
    }
}