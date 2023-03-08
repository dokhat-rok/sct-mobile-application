package com.sct.mobile.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sct.mobile.application.R;
import com.sct.mobile.application.component.observed.impl.AuthObservedImpl;
import com.sct.mobile.application.component.observed.impl.UserObservedImpl;
import com.sct.mobile.application.component.subscriber.AuthSubscriber;
import com.sct.mobile.application.component.subscriber.UserSubscriber;
import com.sct.mobile.application.data.SharedDataUtil;
import com.sct.mobile.application.model.dto.JwtDto;
import com.sct.mobile.application.model.dto.UserDto;
import com.sct.mobile.application.model.enums.SharedName;
import com.sct.mobile.application.service.TokenService;
import com.sct.mobile.application.service.subscriber.TokenSubscriber;

public class LoadingActivity extends AppCompatActivity
        implements TokenSubscriber, AuthSubscriber, UserSubscriber {

    private final TokenService tokenService = TokenService.getInstance();

    private final UserObservedImpl userObserved = new UserObservedImpl();

    private final AuthObservedImpl authObserved = new AuthObservedImpl();

    private Toast toastAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        toastAuth = new Toast(this.getBaseContext());

        SharedDataUtil.init(this.getBaseContext());

        tokenService.subscribeToken(this);
        userObserved.subscribeUser(this);
        authObserved.subscribeAuth(this);
        tokenService.authentication();
    }

    @Override
    public void acceptToken() {
        if(TokenService.getJwt().getToken() == null){
            String login = SharedDataUtil.getString(SharedName.LOGIN.getLabel());
            String password = SharedDataUtil.getString(SharedName.PASSWORD.getLabel());
            if(login.equals("") || password.equals("")) this.checkAuth();
            else this.authorization(login, password);
            return;
        }
        userObserved.getCurrent();
    }

    @Override
    public void errorToken(String error) {
        notification("Сервер недоступен...");
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                tokenService.authentication();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void acceptAuth(JwtDto jwt) {
        Log.d("Jwt token", jwt.getToken());
        if(jwt.getToken() == null){
            this.checkAuth();
            return;
        }
        TokenService.setJwt(jwt);
        userObserved.getCurrent();
//        this.confirmAuth();
    }

    @Override
    public void errorAuth(String error) {
        this.errorToken(error);
    }

    @Override
    public void acceptCurrent(UserDto user) {
        this.confirmAuth();
    }

    @Override
    public void errorUser(String error) {
        this.checkAuth();
    }

    private void notification(String text){
        toastAuth.setText(text);
        toastAuth.show();
    }

    private void confirmAuth(){
        notification("Аутентификация пройдена");
        this.startActivity(new Intent(LoadingActivity.this, MapActivity.class));
        this.finish();
    }

    private void checkAuth(){
        notification("Требуется авторизация");
        this.startActivity(new Intent(LoadingActivity.this, AuthActivity.class));
        this.finish();
    }

    private void authorization(String login, String password){
        authObserved.authorization(login, password);
    }
}