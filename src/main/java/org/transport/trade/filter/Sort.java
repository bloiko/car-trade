package org.transport.trade.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sort {

    private String fieldName;

    private SortOrder sortOrder;
}
