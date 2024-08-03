package org.transport.trade.filter.convertor.impl;

import org.transport.trade.filter.convertor.AbstractTextSearchFilterConverter;

public class ManufacturerCountryFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "manufacturerCountry";
    }
}
