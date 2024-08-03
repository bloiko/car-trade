package org.transport.trade.filter.convertor;

class ModelFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "model";
    }
}
