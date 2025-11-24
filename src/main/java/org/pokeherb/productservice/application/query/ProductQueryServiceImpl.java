package org.pokeherb.productservice.application.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.product.domain.ProductDetailsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductDetailsRepository productDetailsRepository;

    @Override
    public ProductDto getProductById(UUID productId) {

        return productDetailsRepository.getProductById(productId);
    }

    @Override
    public Page<ProductDto> findAllProductByHubId(Long hubId, int page, int size) {

        int pageIdx = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(pageIdx, size);

        return productDetailsRepository.findAllProductByHubId(hubId, pageable);
    }

    @Override
    public Page<ProductDto> findAllProductByDriverId(UUID driverId, int page, int size) {

        int pageIdx = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(pageIdx, size);

        return productDetailsRepository.findAllProductByDriverId(driverId, pageable);
    }


}
