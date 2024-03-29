package com.sct.mobile.application.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    @SerializedName("init")
    @Expose
    private Long init;

    @SerializedName("perMinute")
    @Expose
    private Long perMinute;
}
