package org.transport.trade.controller.convertor.impl;

import org.transport.trade.controller.convertor.AbstractRangeFilterConverter;

public class PriceFilterConverter extends AbstractRangeFilterConverter {

    @Override
    public String getEsFieldId() {
        return "price";
    }
}
