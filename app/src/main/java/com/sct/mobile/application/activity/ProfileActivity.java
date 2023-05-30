package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sct.mobile.application.R;
import com.sct.mobile.application.animation.EmptyInputAnimation;
import com.sct.mobile.application.component.observed.impl.LogoutObservedImpl;
import com.sct.mobile.application.component.observed.impl.UserObservedImpl;
import com.sct.mobile.application.component.subscriber.LogoutSubscriber;
import com.sct.mobile.application.component.subscriber.UserSubscriber;
import com.sct.mobile.application.data.SharedDataUtil;
import com.sct.mobile.application.fragment.AdditionalBalanceFragment;
import com.sct.mobile.application.model.dto.UserDto;
import com.sct.mobile.application.service.TokenService;

public class ProfileActivity extends AppCompatActivity implements UserSubscriber, LogoutSubscriber {

    private EditText loginEdit;

    private EditText balanceEdit;

    private EditText countEdit;

    private Toast toast;

    private LinearLayout additionalBalanceLayout;

    private final UserObservedImpl userObserved = new UserObservedImpl();

    private final LogoutObservedImpl logoutObserved = new LogoutObservedImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_profile);
        this.findViewById(R.id.profile_remove_button).setOnClickListener(this::onCLickRemove);
        this.findViewById(R.id.profile_additional_button)
                .setOnClickListener(this::initAdditionalBalanceLayout);
        this.findViewById(R.id.profile_exit_button).setOnClickListener(this::onClickExit);
        this.findViewById(R.id.profile_delete_button).setOnClickListener(this::onClickDelete);
        toast = new Toast(this.getBaseContext());

        loginEdit = ((TextInputLayout)this.findViewById(R.id.profile_input_login)).getEditText();
        balanceEdit = ((TextInputLayout)this.findViewById(R.id.profile_input_balance)).getEditText();
        countEdit = ((TextInputLayout)this.findViewById(R.id.profile_input_count)).getEditText();

        userObserved.subscribe(this);
        logoutObserved.subscribe(this);
        userObserved.getCurrent();
    }

    public void onClickExit(View view){
        logoutObserved.logout();
    }

    public void initAdditionalBalanceLayout(View view) {
        if(additionalBalanceLayout == null) {
            additionalBalanceLayout = findViewById(R.id.profile_additional_layout);
            AdditionalBalanceFragment fragment = new AdditionalBalanceFragment(this,
                    additionalBalanceLayout);
            additionalBalanceLayout.addView(fragment);
            additionalBalanceLayout.findViewById(R.id.additional_remove_button)
                    .setOnClickListener(this::onRemoveAdditionalBalanceClick);
            additionalBalanceLayout.findViewById(R.id.additional_accept_button)
                    .setOnClickListener(this::onAdditionalBalanceAcceptClick);
        }
    }

    public void onRemoveAdditionalBalanceClick(View view) {
        this.removeAdditionalBalanceFragment();
    }

    public void onAdditionalBalanceAcceptClick(View view) {
        EditText amountText = ((TextInputLayout) additionalBalanceLayout
                .findViewById(R.id.additional_amount)).getEditText();
        assert amountText != null;
        String value = String.valueOf(amountText.getText());
        long amount = 0;
        if (!value.isBlank()) amount = Long.parseLong(value);
        if(amount > 0) userObserved.additionalBalance(amount);
        else this.incorrectAmount(amountText);
    }

    private void incorrectAmount(EditText amountText) {
        EmptyInputAnimation.shake(amountText);
        notification("Сумма пополнения должна быть больше нуля");
    }

    public void onCLickRemove(View view){
        this.startActivity(new Intent(ProfileActivity.this, MapActivity.class));
        this.finish();
    }

    public void onClickDelete(View view) {
        userObserved.deleteCurrent();
    }

    @Override
    public void acceptCurrent(UserDto user) {
        loginEdit.setText(user.getLogin());
        balanceEdit.setText(String.valueOf(user.getBalance()));
        countEdit.setText(String.valueOf(user.getTripCount()));
        this.findViewById(R.id.profile_layout_data).setVisibility(View.VISIBLE);
        this.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
    }

    @Override
    public void errorUser(String error) {
        this.notification(error);
        new Thread(() -> {
           try {
               Thread.sleep(10000);
               userObserved.getCurrent();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        });
    }

    @Override
    public void acceptAdditionalBalance(UserDto user) {
        loginEdit.setText(user.getLogin());
        balanceEdit.setText(String.valueOf(user.getBalance()));
        countEdit.setText(String.valueOf(user.getTripCount()));

        this.notification("Баланс успешно пополнен");
        this.removeAdditionalBalanceFragment();

    }

    @Override
    public void errorAdditionalBalance(String error) {
        UserSubscriber.super.errorAdditionalBalance(error);
    }

    private void removeAdditionalBalanceFragment() {
        additionalBalanceLayout.removeAllViews();
        additionalBalanceLayout = null;
    }

    @Override
    public void acceptLogout() {
        SharedDataUtil.deleteAll();
        TokenService.deleteJwt();
        this.notification("Выход из аккаунта");
        this.startActivity(new Intent(ProfileActivity.this, AuthActivity.class));
        this.finish();
    }

    @Override
    public void errorLogout(String error) {
        this.notification(error);
    }

    @Override
    public void acceptDelete() {
        this.notification("Пользователь успешно удален");
        logoutObserved.logout();
    }

    @Override
    public void errorDelete(String error) {
        this.notification(error);
    }

    private void notification(String text){
        toast.setText(text);
        toast.show();
    }
}