package org.pokeherb.productservice.product.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pokeherb.productservice.product.domain.DeliveryStatus;

import java.util.UUID;

@Entity
@Table(name = "product_driver_map", indexes = @Index(columnList = "driver_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDriverMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID driverId;

    @Enumerated
    private DeliveryStatus deliveryStatus;

    public ProductDriverMap(UUID productId, UUID driverId, DeliveryStatus deliveryStatus) {
        this.productId = productId;
        this.driverId = driverId;
        this.deliveryStatus = deliveryStatus;
    }


    public void updateDriverId(UUID driverId, DeliveryStatus deliveryStatus) {
        this.driverId = driverId;
        this.deliveryStatus = deliveryStatus;
    }
}
