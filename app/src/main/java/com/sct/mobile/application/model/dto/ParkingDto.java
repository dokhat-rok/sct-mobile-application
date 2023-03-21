package com.sct.mobile.application.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sct.mobile.application.model.enums.ParkingType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDto {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("coordinated")
    @Expose
    private String coordinated;

    @SerializedName("allowedRadius")
    @Expose
    private Long allowedRadius;

    @SerializedName("type")
    @Expose
    private ParkingType type;
}
