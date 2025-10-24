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
        Assertions.assertEquals("PC Gamer Terabyte", result.getName());
        Assertions.assertEquals("Lorem ipsum dolor sit amet.", result.getDescription());
        Assertions.assertEquals(4270.0, result.getPrice());
        Assertions.assertEquals("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/25-big.jpg", result.getImgUrl());
    }
}
