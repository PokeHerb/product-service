package org.pokeherb.productservice.product.domain;

import org.pokeherb.productservice.product.domain.entity.ProductDriverMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductDiverMapRepository extends JpaRepository<ProductDriverMap, Long> {


    Optional<ProductDriverMap> findByProductId(UUID productId);
}
