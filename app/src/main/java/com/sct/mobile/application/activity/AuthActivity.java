package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sct.mobile.application.R;
import com.sct.mobile.application.animation.EmptyInputAnimation;
import com.sct.mobile.application.component.subscriber.AuthSubscriber;
import com.sct.mobile.application.model.dto.JwtDto;
import com.sct.mobile.application.component.observed.impl.AuthObservedImpl;
import com.sct.mobile.application.service.TokenService;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity implements AuthSubscriber {

    private Toast toastEntry;

    private Button entryButton;

    private AuthObservedImpl authObserved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_auth);
        entryButton = this.findViewById(R.id.auth_entry_button);
        this.findViewById(R.id.auth_reg_button).setOnClickListener(this::clickOnReg);
        entryButton.setOnClickListener(this::clickOnEntry);
        toastEntry = new Toast(this.getBaseContext());

        authObserved = new AuthObservedImpl();
    }


    public void clickOnEntry(View view){
        entryButton.setClickable(false);
        EditText loginEditText = Objects.requireNonNull(((TextInputLayout)this
                .findViewById(R.id.auth_input_login))
                .getEditText());
        EditText passwordEditText = Objects.requireNonNull(((TextInputLayout)this
                .findViewById(R.id.auth_input_password))
                .getEditText());

        boolean isNonEmptyData = true;
        if(loginEditText.getText().toString().equals("")){
            isNonEmptyData = false;
            EmptyInputAnimation.shake(loginEditText);
        }
        if(passwordEditText.getText().toString().equals("")){
            isNonEmptyData = false;
            EmptyInputAnimation.shake(passwordEditText);
        }

        if(!isNonEmptyData){
            entryButton.setClickable(true);
            return;
        }
        authObserved.subscribeAuth(this);
        authObserved.authorization(loginEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    public void clickOnReg(View view){
        this.startActivity(new Intent(AuthActivity.this, RegistrationActivity.class));
    }

    @Override
    public void acceptAuth(JwtDto jwt) {
        TokenService.setJwt(jwt);
        entryButton.setClickable(true);

        this.notification("Авторизация выполнена");
        this.startActivity(new Intent(AuthActivity.this, MapActivity.class));
        this.finish();
    }

    @Override
    public void errorAuth(String error) {
        this.notification("Ошибка авторизации");
        entryButton.setClickable(true);
    }

    private void notification(String text){
        toastEntry.setText(text);
        toastEntry.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        authObserved.unSubscribeAuth();
    }
}