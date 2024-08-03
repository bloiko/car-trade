package org.transport.trade.transport;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.transport.trade.bodytype.BodyType;
import org.transport.trade.bodytype.BodyTypeRepository;
import org.transport.trade.brand.Brand;
import org.transport.trade.brand.BrandPK;
import org.transport.trade.brand.BrandRepository;
import org.transport.trade.model.Model;
import org.transport.trade.model.ModelRepository;
import org.transport.trade.transport.dto.TransportDto;
import org.transport.trade.transport.rest.TransportRestClient;

import java.util.Arrays;
import java.util.stream.Stream;

@Service
@Transactional
public class TransportDbSynchronizerImpl implements TransportDbSynchronizer {

    private final BodyTypeRepository bodyTypeRepository;

    private final ModelRepository modelRepository;

    private final BrandRepository brandRepository;

    private final TransportRestClient transportRestClient;

    public TransportDbSynchronizerImpl(
            BodyTypeRepository bodyTypeRepository,
            ModelRepository modelRepository,
            BrandRepository brandRepository,
            TransportRestClient transportRestClient) {
        this.bodyTypeRepository = bodyTypeRepository;
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
        this.transportRestClient = transportRestClient;
    }

    @Override
    public void sync() {
        for (int pageNum = 1; pageNum < 10; pageNum++) {
            transportRestClient.getTransports(100, pageNum).stream()
                    .flatMap(transportDto -> {
                        if (transportDto.getBodyType().contains(",")) {
                            return Arrays.stream(transportDto.getBodyType().split(", "))
                                    .map(splittedBodyType -> new TransportDto(
                                            transportDto.getModel(), transportDto.getBrand(), splittedBodyType));
                        }
                        return Stream.of(transportDto);
                    })
                    .forEach(this::saveToDb);
        }
    }

    private void saveToDb(TransportDto transport) {
        boolean shouldBeSynced = false;
        BodyType bodyType = bodyTypeRepository.findByName(transport.getBodyType());

        if (bodyType == null) {
            bodyType = bodyTypeRepository.save(new BodyType(transport.getBodyType()));
            shouldBeSynced = true;
        }

        Model model = modelRepository.findByName(transport.getModel());

        if (model == null) {
            model = modelRepository.save(new Model(transport.getModel()));
            shouldBeSynced = true;
        }

        Brand brand = brandRepository.findBrandByBrandPk(new BrandPK(transport.getBrand(), model, bodyType));

        if (brand == null) {
            brandRepository.save(new Brand(transport.getBrand(), model, bodyType));
        } else if (shouldBeSynced) {
            brand.setBodyType(bodyType);
            brand.setBrand(model);
            brandRepository.save(new Brand(transport.getBrand(), model, bodyType));
        }
    }
}
