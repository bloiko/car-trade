package org.transport.trade.controller.convertor.impl;

import org.transport.trade.controller.convertor.AbstractTextSearchFilterConverter;

public class RegionFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "region_suggest.input";
    }
}
