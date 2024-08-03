package org.transport.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transport {

    private String id;

    private TransportType transportType;

    private String bodyType;

    private Country manufacturerCountry; // save entity in the db

    private int manufacturerYear;

    private String brand; // save entity in the db

    private String model; // save entity in the db

    private BigInteger price;

    private String region;
}

// "properties": {
//        "id": {
//        "type": "text"
//        },
//        "transport_type": {
//        "type": "text"
//        },
//        "body_type": {
//        "type": "text"
//        },
//        "manufacturer_country": {
//        "type": "text"
//        },
//        "manufacturer_year": {
//        "type": "integer"
//        },
//        "brand": {
//        "type": "text"
//        },
//        "model": {
//        "type": "text"
//        },
//        "price": {
//        "type": "double"
//        },
//        "region": {
//        "type": "text"
//        }
//        }
