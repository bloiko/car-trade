package org.transport.trade.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findBrandByBrandPk(BrandPK brandPK);

    List<Brand> findBrandsByBrandPk_BodyType_Name(String bodyTypeName);

    List<Brand> findBrandByBrandPk_BodyType_NameAndBrandPk_Name(String bodyTypeName, String brandName);
}
