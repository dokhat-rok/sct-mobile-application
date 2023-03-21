package com.sct.mobile.application.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sct.mobile.application.model.enums.RentStatus;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RentDto {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("transport")
    @Expose
    private TransportDto transport;

    @SerializedName("customer")
    @Expose
    private UserDto user;

    @SerializedName("beginTimeRent")
    @Expose
    private Timestamp beginTimeRent;

    @SerializedName("endTimeRent")
    @Expose
    private Timestamp endTimeRent;

    @SerializedName("beginParking")
    @Expose
    private ParkingDto beginParking;

    @SerializedName("endParking")
    @Expose
    private ParkingDto endParking;

    @SerializedName("status")
    @Expose
    private RentStatus status;

    @SerializedName("amount")
    @Expose
    private Long amount;
}
