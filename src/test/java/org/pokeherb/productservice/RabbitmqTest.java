package org.pokeherb.productservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.productservice.application.dto.ProductDto;
import org.pokeherb.productservice.infrastructure.rabbit.RabbitConsumer;
import org.pokeherb.productservice.infrastructure.rabbit.RabbitProducer;
import org.pokeherb.productservice.infrastructure.rabbit.dto.OrderCreatedEventDto;
import org.pokeherb.productservice.product.domain.ProductRepository;
import org.pokeherb.productservice.product.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class RabbitmqTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitProducer rabbitProducer;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RabbitConsumer rabbitConsumer;

    @Test
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    @DisplayName("상품 배송 할당 이벤트 메시지 발행 테스트")
    void rabbitmqTest() throws Exception {

        Product product = new Product(UUID.randomUUID(), 1L, "test_product", 1000);
        productRepository.save(product);

        Product foundProduct = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("테스트 데이터 저장 안됨"));

        ProductDto productResponse = ProductDto.from(foundProduct.getVendorId(), foundProduct.getHubId(), foundProduct.getName(), foundProduct.getStock());

        assertDoesNotThrow(() -> {
            rabbitProducer.publishDeliveryEvent(productResponse, "product.delivery.allocated");
        });
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    @DisplayName("상품 배송 이벤트 수신 테스트")
    void rabbitmqOrderCancelTest() throws Exception {

        UUID vendorId = UUID.randomUUID();

        // given -> 재고 10인 상품 생성
        Product product = Product.builder()
                .vendorId(vendorId)
                .hubId(1L)
                .name("cancel_test_product")
                .stock(10)
                .build();


        productRepository.save(product);
        UUID productId = product.getId();
        System.out.println("productId = " + productId);

        // 허브에서 온 주문 이벤트
        OrderCreatedEventDto cancelDto = new OrderCreatedEventDto(productId, 3);

        // when -> rabbitmq로 주문 취소 이벤트 수신 => consumer 메서드 호출
        assertDoesNotThrow(() -> {
            rabbitConsumer.handleOrderEvent(cancelDto);
        });

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("테스트 데이터 저장 안됨"));

        assertThat(updatedProduct.getStock()).isEqualTo(7);

    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    @DisplayName("상품 배송 이벤트 수신 테스트")
    void rabbitmqOrderTest() throws Exception {

        UUID vendorId = UUID.randomUUID();

        // given -> 재고 10인 상품 생성
        Product product = Product.builder()
                .vendorId(vendorId)
                .hubId(1L)
                .name("cancel_test_product")
                .stock(10)
                .build();


        productRepository.save(product);
        UUID productId = product.getId();
        System.out.println("productId = " + productId);

        // 허브에서 온 주문 취소 이벤트
        OrderCreatedEventDto cancelDto = new OrderCreatedEventDto(productId, 11);

        // when -> rabbitmq로 주문 취소 이벤트 수신 => consumer 메서드 호출
        assertDoesNotThrow(() -> {
            rabbitConsumer.handleOrderEvent(cancelDto);
        });

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalArgumentException("테스트 데이터 저장 안됨"));

        assertThat(updatedProduct.getStock()).isEqualTo(product.getStock() );

    }


}
