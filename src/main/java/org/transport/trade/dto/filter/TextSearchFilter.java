package org.transport.trade.dto.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TextSearchFilter extends AbstractFilter {

    private String text;

    public TextSearchFilter(String name, String text) {
        super(name);
        this.text = text;
    }
}
