package org.transport.trade.filter.convertor;

class RegionFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "region_suggest.input";
    }
}
