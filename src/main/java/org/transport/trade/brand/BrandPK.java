package org.transport.trade.brand;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.bodytype.BodyType;
import org.transport.trade.model.Model;

@Embeddable
@Data
@NoArgsConstructor
public class BrandPK implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @ManyToOne
    @JoinColumn(name = "body_type_id", nullable = false)
    private BodyType bodyType;

    public BrandPK(String name, Model model, BodyType bodyType) {
        this.name = name;
        this.model = model;
        this.bodyType = bodyType;
    }
}
