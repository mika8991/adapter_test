package com.solenov.adapterweather.weatherapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Params {
    @JsonAlias("temp")
    private Double currentTemperature;
}
