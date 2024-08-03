package org.transport.trade.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortOrder {
    ASC("Asc"),
    DESC("Desc");

    private final String value;
}
