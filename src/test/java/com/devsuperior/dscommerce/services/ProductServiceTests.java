package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
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
    private ProductDTO dto;
    private Long existingId;
    private Long nonExistingId;
    private Long idIntegrationReference;
    private String productName;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        idIntegrationReference = 3L;
        productName = "PS5";
        product = ProductFactory.createProduct();
        dto = ProductFactory.createProdcutDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.searchByName(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page);
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(any())).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(idIntegrationReference)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(idIntegrationReference);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = productService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
        Assertions.assertEquals(product.getName(), result.getName());
        Assertions.assertEquals(product.getDescription(), result.getDescription());
        Assertions.assertEquals(product.getPrice(), result.getPrice());
        Assertions.assertEquals(product.getImgUrl(), result.getImgUrl());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.findById(nonExistingId));
    }

    @Test
    public void findAllShouldReturnPageProductMinDTO() {

        Pageable pageable = PageRequest.of(0, 12);
        Page<ProductMinDTO> result = productService.findAll(productName, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());
        Assertions.assertEquals(product.getId(), result.iterator().next().getId());
        Assertions.assertEquals(product.getName(), result.iterator().next().getName());
    }

    @Test
    public void insertShouldReturnProductDTO() {

        ProductDTO productDTO = productService.insert(dto);

        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals(1L, productDTO.getId());
        Assertions.assertEquals(dto.getName(), productDTO.getName());
        Assertions.assertEquals(dto.getDescription(), productDTO.getDescription());
        Assertions.assertEquals(dto.getPrice(), productDTO.getPrice());
        Assertions.assertEquals(dto.getImgUrl(), productDTO.getImgUrl());
        Assertions.assertEquals(dto.getCategories().get(0).getId(), productDTO.getCategories().get(0).getId());
        Assertions.assertEquals(dto.getCategories().get(0).getName(), productDTO.getCategories().get(0).getName());

        Mockito.verify(productRepository).save(any());
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistId() {

        ProductDTO productDTO = productService.update(existingId,dto);

        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals(1L, productDTO.getId());
        Assertions.assertEquals(dto.getName(), productDTO.getName());
        Mockito.verify(productRepository).save(any());
    }

    @Test
    public void updateShoulThrowResourceNotFoundExceptionWhenDoesNotExistId() {

       Assertions.assertThrows(ResourceNotFoundException.class,
               () -> {
                   productService.update(nonExistingId,dto);
               });
    }

    @Test
    public void deleteShouldRemoveProductWhenExistingId() {
        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionRemoveProductWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionRemoveProductWhenNonExistingId() {
        Assertions.assertThrows(DatabaseException.class, () -> productService.delete(idIntegrationReference));
    }
}
