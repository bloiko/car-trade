package org.transport.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.entity.Country;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    private String searchText;

    private String transportType;

    private Set<String> bodyTypes;

    private Country manufacturerCountry;

    private YearPeriod manufacturerPeriod;

    private String brand;

    private String model;

    private PriceRange priceRange;

    private String region;

    private Sort sort;

    private int pageSize;
}
