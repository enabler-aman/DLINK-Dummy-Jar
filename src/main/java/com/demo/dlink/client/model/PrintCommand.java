package com.demo.dlink.client.model;
import lombok.Data;

@Data
public class PrintCommand {
    private String jobId;
    private String document;
    private String commandType;
}
