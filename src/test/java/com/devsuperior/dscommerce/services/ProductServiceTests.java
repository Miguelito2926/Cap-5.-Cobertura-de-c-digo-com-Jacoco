package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;
    private Long existId;

    @BeforeEach
    void setUp() throws Exception {

        existId = 1L;
        product = ProductFactory.createProduct();

        Mockito.when(productRepository.findById(existId)).thenReturn(Optional.of(product));
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = productService.findById(existId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existId, result.getId());
        Assertions.assertEquals(product.getName(), result.getName());
        Assertions.assertEquals(product.getDescription(), result.getDescription());
        Assertions.assertEquals(product.getPrice(), result.getPrice());
        Assertions.assertEquals(product.getImgUrl(), result.getImgUrl());
    }
}
