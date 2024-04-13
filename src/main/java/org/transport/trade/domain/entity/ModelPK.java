package org.transport.trade.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class ModelPK implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "body_type_id", nullable = false)
    private BodyType bodyType;

    public ModelPK(String name, Brand brand, BodyType bodyType) {
        this.name = name;
        this.brand = brand;
        this.bodyType = bodyType;
    }
}
