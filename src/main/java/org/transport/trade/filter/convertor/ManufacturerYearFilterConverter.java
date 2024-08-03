package org.transport.trade.filter.convertor;

class ManufacturerYearFilterConverter extends AbstractRangeFilterConverter {

    @Override
    public String getEsFieldId() {
        return "manufacturerYear";
    }
}
