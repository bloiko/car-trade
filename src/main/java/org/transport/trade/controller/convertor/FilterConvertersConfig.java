package org.transport.trade.controller.convertor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.transport.trade.controller.convertor.impl.*;

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
