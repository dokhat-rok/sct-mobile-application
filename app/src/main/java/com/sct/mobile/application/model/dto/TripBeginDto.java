package com.sct.mobile.application.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TripBeginDto {

    @SerializedName("parkingId")
    @Expose
    private Long parkingId;

    @SerializedName("transportId")
    @Expose
    private Long transportId;
}
