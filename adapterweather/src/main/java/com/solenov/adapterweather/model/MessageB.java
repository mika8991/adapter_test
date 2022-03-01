package com.solenov.adapterweather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageB {
    private String text;
    private Date createdDate;
    private Integer currentTemperature;
}
