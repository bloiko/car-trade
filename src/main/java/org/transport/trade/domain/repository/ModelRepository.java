package org.transport.trade.domain.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transport.trade.domain.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    Model findByName(String name);

    List<Model> findModelsByIdIn(Set<Long> modelIds);
}
