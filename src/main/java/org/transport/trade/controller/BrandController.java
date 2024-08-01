package org.transport.trade.controller;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.domain.entity.Brand;
import org.transport.trade.domain.repository.BrandRepository;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping("/body-type/{bodyTypeName}") // TODO move to the request body
    public Set<String> getBrandsByBodyType(@PathVariable("bodyTypeName") String bodyTypeName) {
        return brandRepository.findBrandsByBrandPk_BodyType_Name(bodyTypeName)
                              .stream()
                              .map(Brand::getName)
                              .collect(Collectors.toSet());
    }
}
