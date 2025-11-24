package org.pokeherb.productservice.product.domain;

import org.pokeherb.productservice.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {


}
