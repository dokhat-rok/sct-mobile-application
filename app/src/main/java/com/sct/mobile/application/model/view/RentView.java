package com.sct.mobile.application.model.view;

import com.sct.mobile.application.model.dto.RoutePointDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RentView {

    private Long id;

    private String date;

    private String amount;

    private String transport;

    private String distance;

    private String time;

    private List<RoutePointDto> routePoints;
}
