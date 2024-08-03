package org.transport.trade.filter.convertor;

class PriceFilterConverter extends AbstractRangeFilterConverter {

    @Override
    public String getEsFieldId() {
        return "price";
    }
}
