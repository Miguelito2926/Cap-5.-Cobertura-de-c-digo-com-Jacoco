package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        return new Product(1L, "PC Gamer Terabyte", "Lorem ipsum dolor sit amet.",4270.0,"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/25-big.jpg");
    }

    public static ProductDTO createProdcutDTO() {
        return new ProductDTO(createProduct());
    }
}
