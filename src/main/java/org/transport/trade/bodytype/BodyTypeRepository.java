package org.transport.trade.bodytype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BodyTypeRepository extends JpaRepository<BodyType, Long> {}
