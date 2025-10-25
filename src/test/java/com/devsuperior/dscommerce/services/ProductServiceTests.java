package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
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
    private Long nonExistId;
    private String productName;

    @BeforeEach
    void setUp() throws Exception {

        existId = 1L;
        nonExistId = 2L;
        productName = "PS5";
        product = ProductFactory.createProduct();
        product = ProductFactory.createProduct(productName);

        Mockito.when(productRepository.findById(existId)).thenReturn(Optional.of(product));
      //  Mockito.when(productRepository.findById(existId)).thenReturn(product);
        Mockito.when(productRepository.findById(nonExistId)).thenReturn(Optional.empty());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = productService.findById(existId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
        Assertions.assertEquals(product.getName(), result.getName());
        Assertions.assertEquals(product.getDescription(), result.getDescription());
        Assertions.assertEquals(product.getPrice(), result.getPrice());
        Assertions.assertEquals(product.getImgUrl(), result.getImgUrl());
    }

    @Test
    public void findByIdSholdReturnResourceNotFoundWhenDoesNotExistId() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.findById(nonExistId));
    }
//
//    @Test
//    public void findByIdSholdReturnProductNameWhenExistsId() {
//        Product product = productService.findByProductName(productName);
//    }
}
