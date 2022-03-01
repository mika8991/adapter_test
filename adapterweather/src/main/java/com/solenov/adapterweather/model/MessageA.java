package com.solenov.adapterweather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageA {
    private String message;
    private Language language;
    private Coordinates coordinates;
}
