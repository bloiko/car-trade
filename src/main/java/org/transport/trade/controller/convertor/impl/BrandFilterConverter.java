package org.transport.trade.controller.convertor.impl;

import org.transport.trade.controller.convertor.AbstractTextSearchFilterConverter;

public class BrandFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "brand";
    }
}
