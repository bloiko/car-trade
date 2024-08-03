package org.transport.trade.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.domain.entity.Brand;
import org.transport.trade.domain.repository.BrandRepository;
import org.transport.trade.domain.repository.ModelRepository;
import org.transport.trade.dto.ModelResponse;

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
    public List<ModelResponse> getModels(
            @RequestParam String bodyTypeName, // TODO move to the request body
            @RequestParam String brandName) {
        Set<Long> modelIds =
                brandRepository.findBrandByBrandPk_BodyType_NameAndBrandPk_Name(bodyTypeName, brandName).stream()
                        .map(Brand::getModelId)
                        .collect(Collectors.toSet());

        return modelRepository.findModelsByIdIn(modelIds).stream()
                .map(model -> new ModelResponse(model.getId(), model.getName()))
                .collect(Collectors.toList());
    }
}
