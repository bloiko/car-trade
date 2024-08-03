package org.transport.trade.filter.convertor;

class ManufacturerCountryFilterConverter extends AbstractTextSearchFilterConverter {

    @Override
    public String getEsFieldId() {
        return "manufacturerCountry";
    }
}
