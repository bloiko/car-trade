package org.transport.trade.filter.convertor.impl;

import org.transport.trade.filter.convertor.AbstractRangeFilterConverter;

public class PriceFilterConverter extends AbstractRangeFilterConverter {

    @Override
    public String getEsFieldId() {
        return "price";
    }
}
