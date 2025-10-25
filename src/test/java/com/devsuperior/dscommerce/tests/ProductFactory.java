package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Category category = CategoryFactory.createCategory();
        Product product =  new Product(1L, "PS5", "Lorem ipsum dolor sit amet.",4270.0,"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/25-big.jpg");
        product.getCategories().add(category);

        return product;
    }

    public static ProductDTO createProdcutDTO() {
        return new ProductDTO(createProduct());
    }
}
