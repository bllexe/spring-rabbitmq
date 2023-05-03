package com.tigris.springmysqlrabbitmq.dto;

import com.tigris.springmysqlrabbitmq.commands.ProductForm;
import com.tigris.springmysqlrabbitmq.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class ProductToProductForm implements Converter<Product, ProductForm> {


    @Override
    public ProductForm convert(Product product) {
        ProductForm productForm = new ProductForm();
        productForm.setId(product.getId());
        productForm.setDescription(product.getDescription());
        productForm.setPrice(product.getPrice());
        productForm.setImageUrl(product.getImageUrl());
        return productForm;
    }
}
