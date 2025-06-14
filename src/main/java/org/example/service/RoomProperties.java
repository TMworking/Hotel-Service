package org.example.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class RoomProperties {

    @Value("${statusChangeEnabled}")
    private Boolean statusChangeEnabled;
    @Value("${historySize}")
    private Integer historySize;
}
