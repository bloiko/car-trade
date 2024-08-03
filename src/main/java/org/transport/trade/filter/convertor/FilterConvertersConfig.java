package org.transport.trade.filter.convertor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConvertersConfig {

    @Bean
    Map<String, FilterConverter> filterConverters() {
        Map<String, FilterConverter> filterConvertors = new HashMap<>();
        filterConvertors.put("transportType", new TransportTypeFilterConverter());
        filterConvertors.put("brand", new BrandFilterConverter());
        filterConvertors.put("model", new ModelFilterConverter());
        filterConvertors.put("bodyType", new BodyTypeFilterConverter());
        filterConvertors.put("manufacturerCountry", new ManufacturerCountryFilterConverter());
        filterConvertors.put("price", new PriceFilterConverter());
        filterConvertors.put("manufacturerYear", new ManufacturerYearFilterConverter());

        return filterConvertors;
    }
}
