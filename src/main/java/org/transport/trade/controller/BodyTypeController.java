package org.transport.trade.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transport.trade.domain.repository.BodyTypeRepository;
import org.transport.trade.dto.BodyTypeResponse;

@RestController
@RequestMapping("/body-types")
public class BodyTypeController {

    private final BodyTypeRepository bodyTypeRepository; // move to the service

    @Autowired
    public BodyTypeController(BodyTypeRepository bodyTypeRepository) {
        this.bodyTypeRepository = bodyTypeRepository;
    }

    @GetMapping
    public List<BodyTypeResponse> getAllBodyTypes() {
        return bodyTypeRepository.findAll()
                                 .stream()
                                 .map(bodyType -> new BodyTypeResponse(bodyType.getId(), bodyType.getName()))
                                 .collect(Collectors.toList());
    }
}
