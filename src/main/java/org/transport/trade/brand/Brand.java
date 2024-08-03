package org.transport.trade.brand;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transport.trade.bodytype.BodyType;
import org.transport.trade.model.Model;

@Entity
@Table(name = "brand")
@Data
@NoArgsConstructor
public class Brand {

    @EmbeddedId
    private BrandPK brandPk;

    public Brand(String name, Model model, BodyType bodyType) {
        this.brandPk = new BrandPK(name, model, bodyType);
    }

    public void setBodyType(BodyType bodyType) {
        this.brandPk.setBodyType(bodyType);
    }

    public String getName() {
        return this.brandPk.getName();
    }

    public String getBodyType() {
        return this.brandPk.getBodyType().getName();
    }

    public void setBrand(Model model) {
        this.brandPk.setModel(model);
    }

    public Long getModelId() {
        return this.brandPk.getModel().getId();
    }

    public String getModelName() {
        return this.brandPk.getModel().getName();
    }
}
