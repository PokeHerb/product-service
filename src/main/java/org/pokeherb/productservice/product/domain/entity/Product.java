package org.pokeherb.productservice.product.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.global.domain.Auditable;
import org.pokeherb.productservice.global.infrastructure.client.HubServiceClient;
import org.pokeherb.productservice.global.infrastructure.client.VendorServiceClient;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.pokeherb.productservice.infrastructure.dto.VendorResponse;
import org.pokeherb.productservice.product.domain.VendorHubIdDto;
import org.pokeherb.productservice.product.presentation.dto.ProductUpdateRequestDto;
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
    public Product(UUID vendorId, Long hubId, String name, int stock) {
        this.vendorId = vendorId;
        this.hubId = hubId;
        this.name = name;
        this.stock = stock;
    }

    public void delete(String username) {
        softDelete(username);
    }

    public void changeStock(int stockNumber) {
        this.stock = stockNumber;
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

    public VendorHubIdDto setVendorIdHubId(UUID vendorId, VendorServiceClient vendorClient) {

        VendorResponse vendorResponse = vendorCheck(vendorId, vendorClient);
        if (vendorResponse.vendorId() == null) {
            throw new CustomException(ProductErrorCode.VENDOR_NOT_FOUND);
        }

        if (vendorResponse.hubId() == null) {
            throw new CustomException(ProductErrorCode.HUB_NOT_FOUND);
        }

        this.vendorId = vendorResponse.vendorId();
        this.hubId = vendorResponse.hubId();

        return new VendorHubIdDto(vendorResponse.vendorId(), vendorResponse.hubId());

    }

    public VendorResponse vendorCheck(UUID vendorId, VendorServiceClient vendorClient) {
        return vendorClient.getVendor(vendorId);
    }

    public void changeInfo(ProductUpdateRequestDto dto) {
        this.vendorId = dto.vendorId();
        this.name = dto.name();
        this.stock = dto.stock();
    }

    public ProductDto toDto() {

        return ProductDto.builder()
                .productId(this.id)
                .vendorId(this.vendorId)
                .hubId(this.hubId)
                .name(this.name)
                .stock(this.stock)
                .build();
    }
}
