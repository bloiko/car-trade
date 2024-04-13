package org.transport.trade.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.transport.trade.domain.entity.BodyType;
import org.transport.trade.domain.entity.Brand;
import org.transport.trade.domain.entity.Model;
import org.transport.trade.domain.entity.ModelPK;
import org.transport.trade.domain.repository.BodyTypeRepository;
import org.transport.trade.domain.repository.BrandRepository;
import org.transport.trade.domain.repository.ModelRepository;
import org.transport.trade.service.rest.TransportRestClient;
import org.transport.trade.service.rest.dto.TransportDto;


@Service
@Transactional
public class TransportDbSynchronizerImpl implements TransportDbSynchronizer {

    private final BodyTypeRepository bodyTypeRepository;

    private final ModelRepository modelRepository;

    private final BrandRepository brandRepository;

    private final TransportRestClient transportRestClient;

    public TransportDbSynchronizerImpl(BodyTypeRepository bodyTypeRepository, ModelRepository modelRepository, BrandRepository brandRepository, TransportRestClient transportRestClient) {
        this.bodyTypeRepository = bodyTypeRepository;
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
        this.transportRestClient = transportRestClient;
    }

    @Override
    public void sync() {
        for (int pageNum = 1; pageNum < 2; pageNum++) {
            transportRestClient.getTransports(100, pageNum).forEach(this::saveToDb);
        }
    }

    private void saveToDb(TransportDto transport) {
        boolean shouldBeSynced = false;
        BodyType bodyType = bodyTypeRepository.findByName(transport.getBodyType());

        if (bodyType == null) {
            bodyType = bodyTypeRepository.save(new BodyType(transport.getBodyType()));
            shouldBeSynced = true;
        }

        Brand brand = brandRepository.findByName(transport.getBrand());

        if (brand == null) {
            brand = brandRepository.save(new Brand(transport.getBrand()));
            shouldBeSynced = true;
        }

        Model model = modelRepository.findModelByModelPk(new ModelPK(transport.getModel(), brand, bodyType));

        if (model == null) {
            modelRepository.save(new Model(transport.getModel(), brand, bodyType));
        } else if (shouldBeSynced) {
            model.setBodyType(bodyType);
            model.setBrand(brand);
            modelRepository.save(new Model(transport.getModel(), brand, bodyType));
        }
    }
}
