package org.pokeherb.productservice.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.productservice.product.domain.ProductDiverMapRepository;
import org.pokeherb.productservice.product.domain.entity.ProductDriverMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryEventListener {

    private final ProductDiverMapRepository productDriverMapRepository;

    // RabbitMQ 관련 수정 필요
    @RabbitListener()
    public void handleDeliveryEvent(DeliveryAllocatedEvent event) {
        log.info("받은 Event: {}", event);

        productDriverMapRepository.findByProductId(event.productId())
                .ifPresentOrElse(
                        map -> map.updateDriverId(event.driverId(), event.deliveryStatus()),
                        () -> productDriverMapRepository.save(new ProductDriverMap(event.productId(), event.driverId(), event.deliveryStatus()))

                );

    }
}
