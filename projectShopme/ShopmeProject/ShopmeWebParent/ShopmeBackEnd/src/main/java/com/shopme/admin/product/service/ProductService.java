package com.shopme.admin.product.service;

import com.shopme.admin.product.exception.ProductNotFoundException;
import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.common.entity.Product;
import org.apache.poi.ss.formula.functions.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public List<Product> listAll(){
        return (List<Product>) productRepository.findAll();
    }

    public Product save(Product product){
        if (product.getId() == null){
            product.setCreatedTime(new Date());
        }
        if (product.getAlias()==null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replace(" ","-");
            product.setAlias(defaultAlias);
        }else {
            product.setAlias(product.getAlias().replace(" ","-"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    public String checkUnique(Integer id,String name){
        boolean isCreatingNew = (id==null || id == 0);
        Product productName = productRepository.findByName(name);
        if (isCreatingNew) {
                if (productName != null) return "Duplicate";
        }else {
            if (productName != null && productName.getId() != id){
                return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateProductEnabledStatus(Integer id,boolean enabled){
        productRepository.updateEnabledStatus(id,enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if (Objects.isNull(countById)){
            throw new ProductNotFoundException("Could not find any product with ID "+id);
        }
        productRepository.deleteById(id);
    }
}
