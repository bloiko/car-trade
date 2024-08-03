package org.transport.trade.filter.convertor.impl;

import org.transport.trade.filter.convertor.AbstractTextSearchFilterConverter;

public class BrandFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "brand";
    }
}
