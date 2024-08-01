package org.transport.trade.dto.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = TextSearchFilter.class, name = "TEXT_SEARCH"), @JsonSubTypes.Type(value = RangeFilter.class, name = "RANGE")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractFilter {

    @NonNull
    private String name;
}
