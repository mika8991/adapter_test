package com.solenov.adapterweather;

import com.solenov.adapterweather.model.Language;
import com.solenov.adapterweather.model.MessageA;
import com.solenov.adapterweather.model.MessageB;
import com.solenov.adapterweather.weatherapi.OpenWeather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class WeatherService {
    @Value("${openWeatherApiKey}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MessageB addWeatherDataToMessage(MessageA messageA) throws Exception {
        if (messageA != null && messageA.getLanguage().equals(Language.RU) && !messageA.getMessage().isEmpty()) {
            OpenWeather weatherApi = getWeatherApi(messageA);
            if (weatherApi != null) {
                weatherApi.fillParameters();
                MessageB messageB = new MessageB();
                messageB.setText(messageA.getMessage());
                messageB.setCreatedDate(new Date());
                messageB.setCurrentTemperature(weatherApi.getCurrentTemperature());
                return messageB;
            } else {
                throw new Exception();
            }
        }
        return null;
    }

    private OpenWeather getWeatherApi(MessageA messageA) {
        String latitude = messageA.getCoordinates().getLatitude();
        String longitude = messageA.getCoordinates().getLongitude();
        if (!latitude.isEmpty() && !longitude.isEmpty()) {
            String uri = String.format("http://api.openweathermap.org/data/2.5/weather?" +
                            "lat=%s&" +
                            "lon=%s&" +
                            "lang=ru&" +
                            "units=metric&" +
                            "appid=%s",
                    latitude,
                    longitude,
                    apiKey);
            return restTemplate.getForObject(uri, OpenWeather.class);
        } else {
            return null;
        }
    }
}
