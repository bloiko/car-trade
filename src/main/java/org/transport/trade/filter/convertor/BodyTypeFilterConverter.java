package org.transport.trade.filter.convertor;

class BodyTypeFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "bodyType";
    }
}
