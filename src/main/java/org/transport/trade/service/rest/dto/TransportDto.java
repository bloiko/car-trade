package org.transport.trade.service.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportDto {

    @JsonProperty("Model")
    private String model;

    @JsonProperty("Make")
    private String brand;

    @JsonProperty("Category")
    private String bodyType;
}
