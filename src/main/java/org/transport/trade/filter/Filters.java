package org.transport.trade.filter;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filters {

    private List<AbstractFilter> filters = new ArrayList<>();

    private Sort sort;

    private Integer pageSize;
}
