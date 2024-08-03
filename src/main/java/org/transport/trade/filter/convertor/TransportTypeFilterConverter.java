package org.transport.trade.filter.convertor;

class TransportTypeFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "transportType";
    }
}
