package org.transport.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transport.trade.domain.entity.BodyType;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {

    BodyType findByName(String name);
}
