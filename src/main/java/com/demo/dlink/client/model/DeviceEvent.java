package com.demo.dlink.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceEvent {
    private String stationId;
    private String type;
    private String deviceName;
    private String payload;
}
