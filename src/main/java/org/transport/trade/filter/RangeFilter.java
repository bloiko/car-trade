package org.transport.trade.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RangeFilter extends AbstractFilter {

    private String from;

    private String to;

    public RangeFilter(String name, String from, String to) {
        super(name);
        this.from = from;
        this.to = to;
    }
}
