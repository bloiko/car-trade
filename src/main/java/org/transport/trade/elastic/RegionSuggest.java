package org.transport.trade.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionSuggest {

    private List<String> input;

    public RegionSuggest(String singleInput) {
        this.input = new ArrayList<>();
        this.input.add(singleInput);
    }
}
