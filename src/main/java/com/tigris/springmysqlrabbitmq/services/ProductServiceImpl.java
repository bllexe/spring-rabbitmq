package com.tigris.springmysqlrabbitmq.services;

import com.tigris.springmysqlrabbitmq.SpringMysqlRabbitmqApplication;
import com.tigris.springmysqlrabbitmq.commands.ProductForm;
import com.tigris.springmysqlrabbitmq.dto.ProductFormToProduct;
import com.tigris.springmysqlrabbitmq.listener.ProductMessageListener;
import com.tigris.springmysqlrabbitmq.model.Product;
import com.tigris.springmysqlrabbitmq.repository.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);


    private final ProductRepository productRepository;
    private final ProductFormToProduct productFormToProduct;
    private final RabbitTemplate rabbitTemplate;

    private final ProductMessageListener productMessageListener;

    public ProductServiceImpl(ProductRepository productRepository, ProductFormToProduct productFormToProduct, RabbitTemplate rabbitTemplate, ProductMessageListener productMessageListener) {
        this.productRepository = productRepository;
        this.productFormToProduct = productFormToProduct;
        this.rabbitTemplate = rabbitTemplate;
        this.productMessageListener = productMessageListener;
    }

    @Override
    public List<Product> listAll() {
        List<Product> products=new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {

        productRepository.deleteById(id);
    }

    @Override
    public Product saveOrUpdateProductForm(ProductForm productForm) {

        Product savedProduct=saveOrUpdate(productFormToProduct.convert(productForm));
        System.out.println("saved product id:" + savedProduct.getId());
        return savedProduct;

    }

    @Override
    public void sendProductMessage(String id) {

        Map<String,String> actionmap=new HashMap<>();
        actionmap.put("id",id);
        rabbitTemplate.convertAndSend(SpringMysqlRabbitmqApplication.SFG_MESSAGE_QUEUE, actionmap);
        productMessageListener.receiveMessage(actionmap);

    }
}
