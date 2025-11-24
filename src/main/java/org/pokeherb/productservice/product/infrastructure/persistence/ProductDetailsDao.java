package org.pokeherb.productservice.product.infrastructure.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.global.infrastructure.exception.CustomException;
import org.pokeherb.productservice.product.domain.ProductDetailsRepository;
import org.pokeherb.productservice.product.domain.entity.Product;
import org.pokeherb.productservice.product.domain.entity.QProduct;
import org.pokeherb.productservice.product.domain.entity.QProductDriverMap;
import org.pokeherb.productservice.product.domain.exception.ProductErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductDetailsDao implements ProductDetailsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ProductDto getProductById(UUID productId) {

        QProduct product = QProduct.product;
        Product foundProduct = queryFactory.selectFrom(product)
                .where(product.id.eq(productId)
                        , product.deletedAt.isNull())
                .fetchOne();

        return Optional.ofNullable(foundProduct)
                .map(Product::toDto)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public Page<ProductDto> findAllProductByHubId(Long hubId, Pageable pageable) {

        QProduct product = QProduct.product;

        // projections 사용하여 DTO로 바로 매핑
        List<ProductDto> productDtoList = queryFactory.select(Projections.constructor(ProductDto.class,
                        product.id,
                        product.vendorId,
                        product.hubId,
                        product.name,
                        product.stock,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .where(product.hubId.eq(hubId), product.deletedAt.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(product.hubId.eq(hubId), product.deletedAt.isNull());

        return PageableExecutionUtils.getPage(productDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ProductDto> findAllProductByDriverId(UUID driverId, Pageable pageable) {

        QProduct product = QProduct.product;
        QProductDriverMap productDriverMap = QProductDriverMap.productDriverMap;

        List<ProductDto> productDtoList = queryFactory.select(Projections.constructor(ProductDto.class,
                        product.id,
                        product.vendorId,
                        product.hubId,
                        product.name,
                        product.stock,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .join(productDriverMap).on(product.id.eq(productDriverMap.productId))
                .where(productDriverMap.driverId.eq(driverId), product.deletedAt.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetch();

        // count
        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .join(productDriverMap).on(product.id.eq(productDriverMap.productId))
                .where(productDriverMap.driverId.eq(driverId), product.deletedAt.isNull());

        return PageableExecutionUtils.getPage(productDtoList, pageable, countQuery::fetchOne);
    }

}
