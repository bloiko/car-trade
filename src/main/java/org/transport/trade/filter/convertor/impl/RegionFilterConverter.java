package org.transport.trade.filter.convertor.impl;

import org.transport.trade.filter.convertor.AbstractTextSearchFilterConverter;

public class RegionFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "region_suggest.input";
    }
}
