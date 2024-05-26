package org.transport.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transport.trade.domain.entity.Brand;
import org.transport.trade.domain.entity.BrandPK;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findBrandByBrandPk(BrandPK brandPK);

    List<Brand> findBrandsByBrandPk_BodyType_Name(String bodyTypeName);

    List<Brand> findBrandByBrandPk_BodyType_NameAndBrandPk_Name(String bodyTypeName, String brandName);
}
