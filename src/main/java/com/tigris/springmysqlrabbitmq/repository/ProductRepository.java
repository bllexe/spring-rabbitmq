package com.tigris.springmysqlrabbitmq.repository;

import com.tigris.springmysqlrabbitmq.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long> {
}
