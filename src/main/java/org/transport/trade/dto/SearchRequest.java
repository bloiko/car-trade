package org.transport.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.entity.BodyType;
import org.transport.trade.entity.Brand;
import org.transport.trade.entity.Country;
import org.transport.trade.entity.TransportType;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    private TransportType transportType;

    private Set<BodyType> bodyTypes;

    private Country manufacturerCountry;

    private YearPeriod manufacturerPeriod;

    private Brand brand;

    private String model;

    private PriceRange priceRange;

    private String region;

    private Sort sort;

    private int pageSize;
}
