package org.transport.trade.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "model")
@Data
@NoArgsConstructor
public class Model {

    @EmbeddedId
    private ModelPK modelPk;

    public Model(String name, Brand brand, BodyType bodyType) {
        this.modelPk = new ModelPK(name, brand, bodyType);
    }

    public void setBodyType(BodyType bodyType) {
        this.modelPk.setBodyType(bodyType);
    }

    public void setBrand(Brand brand) {
        this.modelPk.setBrand(brand);
    }
}
