package org.pokeherb.productservice.infrastructure.event;

import org.pokeherb.productservice.product.domain.DeliveryStatus;

import java.util.UUID;

/**
 * 배송 할당 완료 이벤트
 * 메시지 큐를 통해 주고 받을 이벤트 객체
 */
public record DeliveryAllocatedEvent(

        UUID productId,
        UUID driverId,
        DeliveryStatus deliveryStatus


) {
}
