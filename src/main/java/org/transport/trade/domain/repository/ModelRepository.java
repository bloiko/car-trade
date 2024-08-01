package org.transport.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transport.trade.domain.entity.Model;

import java.util.List;
import java.util.Set;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    Model findByName(String name);

    List<Model> findModelsByIdIn(Set<Long> modelIds);
}
