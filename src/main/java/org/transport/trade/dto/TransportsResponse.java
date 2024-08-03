package org.transport.trade.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.entity.Transport;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportsResponse {

    private List<Transport> transports;

    private long total;
}
