package com.solenov.adapterweather.weatherapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;


@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenWeather {

    @JsonAlias("main")
    private Params mainWeatherParameters;
    private Integer currentTemperature;

    public void fillParameters() {
        if (mainWeatherParameters != null) {
            this.setCurrentTemperature(mainWeatherParameters.getCurrentTemperature().intValue());
        }
    }
}
