package org.transport.trade.brand;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping("/body-type/{bodyTypeName}") // TODO move to the request body
    Set<String> getBrandsByBodyType(@PathVariable("bodyTypeName") String bodyTypeName) {
        return brandRepository.findBrandsByBrandPk_BodyType_Name(bodyTypeName).stream()
                .map(Brand::getName)
                .collect(Collectors.toSet());
    }
}
