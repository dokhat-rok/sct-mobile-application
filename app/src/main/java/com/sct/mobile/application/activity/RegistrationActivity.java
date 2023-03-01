package com.sct.mobile.application.activity;

import androidx.annotation.NonNull;
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
import com.sct.mobile.application.client.AuthApi;
import com.sct.mobile.application.config.NetworkService;
import com.sct.mobile.application.model.dto.UserDto;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private Toast toastReg;

    private final AuthApi authApi = NetworkService.getInstance().getAuthApi();

    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        regButton = this.findViewById(R.id.reg_reg_button);
        regButton.setOnClickListener(this::onClick);
        toastReg = new Toast(this.getBaseContext());
    }

    public void onClick(View view){
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

        authApi.registration(
                loginEditText.getText().toString(),
                passEditText.getText().toString(),
                confPassEditText.getText().toString()).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<UserDto> call,
                    @NonNull Response<UserDto> response) {
                RegistrationActivity.this.confirmRegistration(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                RegistrationActivity.this.failureRegistration();
            }
        });
    }

    private void confirmRegistration(UserDto user){
        if(user == null) return;

        this.notification(user.getLogin() + ", регистрация выполнена успешна");
        this.startActivity(new Intent(RegistrationActivity.this, AuthActivity.class));
    }

    private void failureRegistration(){
        this.notification("Регистрация не получилась. Попробуйте ещё раз");
        regButton.setClickable(true);
    }

    private void notification(String text){
        toastReg.setText(text);
        toastReg.show();
    }
}