package org.transport.trade.filter.convertor.impl;

import org.transport.trade.filter.convertor.AbstractTextSearchFilterConverter;

public class ModelFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "model";
    }
}
