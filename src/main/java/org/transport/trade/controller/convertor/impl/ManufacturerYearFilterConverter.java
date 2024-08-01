package org.transport.trade.controller.convertor.impl;

import org.transport.trade.controller.convertor.AbstractRangeFilterConverter;

public class ManufacturerYearFilterConverter extends AbstractRangeFilterConverter {

    @Override
    public String getEsFieldId() {
        return "manufacturerYear";
    }
}
