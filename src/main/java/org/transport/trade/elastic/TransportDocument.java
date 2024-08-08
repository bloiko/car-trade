package org.transport.trade.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.transport.entity.Country;
import org.transport.trade.transport.entity.TransportType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportDocument {

    private String id;

    private TransportType transportType;

    private String bodyType;

    private Country manufacturerCountry; // save entity in the db

    private int manufacturerYear;

    private String brand; // save entity in the db

    private String model; // save entity in the db

    private BigInteger price;

    private String region;

    @JsonProperty("region_suggest")
    private RegionSuggest regionSuggest;
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
