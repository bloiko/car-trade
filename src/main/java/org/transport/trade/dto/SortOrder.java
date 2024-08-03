package org.transport.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortOrder {
    ASC("Asc"),
    DESC("Desc");

    private final String value;
}
