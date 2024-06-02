package org.transport.trade.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.dto.Sort;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filters {

    private List<AbstractFilter> filters = new ArrayList<>();

    private Sort sort;

    private Integer pageSize;
}
