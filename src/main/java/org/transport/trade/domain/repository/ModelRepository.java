package org.transport.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transport.trade.domain.entity.Model;
import org.transport.trade.domain.entity.ModelPK;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    Model findModelByModelPk(ModelPK modelPK);
}
