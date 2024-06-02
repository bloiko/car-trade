package org.transport.trade.controller.convertor.impl;

import org.transport.trade.controller.convertor.AbstractTextSearchFilterConverter;

public class ModelFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "model";
    }
}
