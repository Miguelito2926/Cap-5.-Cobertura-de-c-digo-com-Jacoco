package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

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
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {

        existId = 1L;
        nonExistId = 2L;
        productName = "PS5";
        product = ProductFactory.createProduct();
        product = ProductFactory.createProduct(productName);
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.findById(existId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.searchByName(any(), any())).thenReturn(page);
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

    @Test
    public void findAllSholdReturnPageProductMinDTO() {
        Pageable pageable = PageRequest.of(0,12);
        String name = "PS5";
        Page <ProductMinDTO> page = productService.findAll(name, pageable);

        Assertions.assertNotNull(page);
    }
}
