package org.transport.trade.filter.convertor.impl;

import org.transport.trade.filter.convertor.AbstractTextSearchFilterConverter;

public class BodyTypeFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "bodyType";
    }
}
