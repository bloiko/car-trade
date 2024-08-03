package org.transport.trade.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.brand.Brand;
import org.transport.trade.brand.BrandRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/models")
public class ModelController {

    private final ModelRepository modelRepository;

    private final BrandRepository brandRepository;

    @Autowired
    public ModelController(ModelRepository modelRepository, BrandRepository brandRepository) {
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping
    List<ModelResponse> getModels(@RequestParam String bodyTypeName, @RequestParam String brandName) {
        Set<Long> modelIds =
                brandRepository.findBrandByBrandPk_BodyType_NameAndBrandPk_Name(bodyTypeName, brandName).stream()
                        .map(Brand::getModelId)
                        .collect(Collectors.toSet());

        return modelRepository.findModelsByIdIn(modelIds).stream()
                .map(model -> new ModelResponse(model.getId(), model.getName()))
                .collect(Collectors.toList());
    }
}
