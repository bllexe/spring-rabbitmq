package com.tigris.springmysqlrabbitmq.services;

import com.tigris.springmysqlrabbitmq.commands.ProductForm;
import com.tigris.springmysqlrabbitmq.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> listAll();
    Product getById(Long id);
    Product saveOrUpdate(Product product);
    void delete(Long id);
    Product saveOrUpdateProductForm(ProductForm productForm);
    void sendProductMessage(String id);


}
