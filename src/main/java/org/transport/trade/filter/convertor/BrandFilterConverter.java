package org.transport.trade.filter.convertor;

class BrandFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "brand";
    }
}
