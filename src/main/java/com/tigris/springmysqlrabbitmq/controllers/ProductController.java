package com.tigris.springmysqlrabbitmq.controllers;

import com.tigris.springmysqlrabbitmq.commands.ProductForm;
import com.tigris.springmysqlrabbitmq.dto.ProductToProductForm;
import com.tigris.springmysqlrabbitmq.listener.ProductMessageListener;
import com.tigris.springmysqlrabbitmq.model.Product;
import com.tigris.springmysqlrabbitmq.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProductController {

    private static final Logger log = LogManager.getLogger(ProductController.class);
    private final ProductService productService;

    private final ProductToProductForm productToProductForm;

    public ProductController(ProductService productService,ProductToProductForm productToProductForm) {
        this.productService = productService;
        this.productToProductForm = productToProductForm;
    }

    @RequestMapping("/")
    public String redirToList(){
        return "redirect:/product/list";
    }

    @RequestMapping({"/product/list", "/product"})
    public String listProducts(Model model){
        model.addAttribute("products", productService.listAll());
        return "product/list";
    }

    @RequestMapping("/product/show/{id}")
    public String getProduct(@PathVariable String id, Model model){
        model.addAttribute("product", productService.getById(Long.valueOf(id)));
        return "product/show";
    }

    @RequestMapping("product/edit/{id}")
    public String edit(@PathVariable String id, Model model){
        Product product = productService.getById(Long.valueOf(id));
        ProductForm productForm = productToProductForm.convert(product);

        model.addAttribute("productForm", productForm);
        return "product/productform";
    }

    @RequestMapping("/product/new")
    public String newProduct(Model model){
        model.addAttribute("productForm", new ProductForm());
        return "product/productform";
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public String saveOrUpdateProduct(ProductForm productForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "product/productform";
        }

        Product savedProduct = productService.saveOrUpdateProductForm(productForm);

        return "redirect:/product/show/" + savedProduct.getId();

    }

    @RequestMapping("/product/delete/{id}")
    public String delete(@PathVariable String id){
        productService.delete(Long.valueOf(id));
        return "redirect:/product/list";
    }

    @RequestMapping("/product/indexProduct/{id}")
    public String indexProduct(@PathVariable String id){
        productService.sendProductMessage(id);

        return "redirect:/product/show/" + id;
    }

}
