package com.sct.mobile.application.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sct.mobile.application.model.enums.Condition;
import com.sct.mobile.application.model.enums.TransportStatus;
import com.sct.mobile.application.model.enums.TransportType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportDto {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("type")
    @Expose
    private TransportType type;

    @SerializedName("identificationNumber")
    @Expose
    private String identificationNumber;

    @SerializedName("coordinates")
    @Expose
    private String coordinates;

    @SerializedName("condition")
    @Expose
    private Condition condition;

    @SerializedName("status")
    @Expose
    private TransportStatus status;

    @SerializedName("chargePercentage")
    @Expose
    private Long chargePercentage;

    @SerializedName("maxSpeed")
    @Expose
    private Long maxSpeed;

    @SerializedName("parking")
    @Expose
    private ParkingNameDto parking;
}
