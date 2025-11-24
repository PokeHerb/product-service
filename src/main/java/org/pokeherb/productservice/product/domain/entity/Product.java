package org.pokeherb.productservice.product.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pokeherb.productservice.global.domain.Auditable;
import org.pokeherb.productservice.global.infrastructure.client.HubServiceClient;
import org.pokeherb.productservice.global.infrastructure.client.VendorServiceClient;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.pokeherb.productservice.product.domain.application.dto.ProductUpdateRequestDto;
import org.pokeherb.productservice.product.domain.exception.ProductErrorCode;
import org.pokeherb.productservice.product.domain.exception.ProductStockNotEnoughException;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_product")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID vendorId;

    private Long hubId;

    private String name;

    private int stock;

    @Builder
    public Product(UUID vendorId, Long hubId, String name, Integer stock) {
        this.vendorId = vendorId;
        this.hubId = hubId;
        this.name = name;
        this.stock = stock;
    }

    public void delete(String username) {
        softDelete(username);
    }

    public void decreaseStock(int orderCount) {
        if(this.stock <= 0 || this.stock - orderCount < 0) {
            throw new ProductStockNotEnoughException(this.stock);
        }
        this.stock -= orderCount;
    }

    public boolean existsByIds(Long hubId, UUID vendorId, HubServiceClient hubClient, VendorServiceClient vendorClient) {
        if (!hubClient.existsHub(hubId)) {
            throw new CustomException(ProductErrorCode.HUB_NOT_FOUND);
        }

        if (!vendorClient.existsVendor(vendorId)) {
            throw new CustomException(ProductErrorCode.HUB_NOT_FOUND);
        }

        return true;
    }

    public void changeInfo(ProductUpdateRequestDto dto) {
        this.hubId = dto.hubId();
        this.name = dto.name();
        this.stock = dto.stock();
    }

}
