package org.transport.trade.model;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ModelRepository extends JpaRepository<Model, Long> {

    Model findByName(String name);

    List<Model> findModelsByIdIn(Set<Long> modelIds);
}
