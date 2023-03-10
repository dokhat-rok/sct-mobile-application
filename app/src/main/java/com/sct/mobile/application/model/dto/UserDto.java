package com.sct.mobile.application.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.sct.mobile.application.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("balance")
    @Expose
    private Long balance;

    @SerializedName("role")
    @Expose
    private Role role;

    @SerializedName("tripCount")
    @Expose
    private Long tripCount;
}
