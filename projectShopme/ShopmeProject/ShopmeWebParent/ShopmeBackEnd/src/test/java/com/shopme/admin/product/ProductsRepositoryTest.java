package com.shopme.admin.product;


import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductsRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class,38);
        Category category = entityManager.find(Category.class,7);

        Product product = new Product();
        product.setName("dell inspiron 3000");
        product.setAlias("dell_3000");
        product.setShortDescription("Short description for dell");
        product.setFullDescription("full description for dell");
        product.setBrand(brand);
        product.setCategory(category);

        product.setCost(300);
        product.setEnabled(true);
        product.setHeight(2000);
        product.setPrice(5000);
        product.setLength(200);
        product.setWeight(90);
        product.setWidth(120);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product saveProduct = productRepository.save(product);

        assertThat(saveProduct).isNotNull();
        assertThat(saveProduct.getId()).isGreaterThan(0);

    }
    @Test
    public void  testListALllProducts(){

        Iterable<Product> productIterable = productRepository.findAll();
        productIterable.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;

        Product product = productRepository.findById(id).get();

        System.out.println(product);
        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 2;

        Product product = productRepository.findById(id).get();

        product.setCost(500);
        productRepository.save(product);
        Product product1 = entityManager.find(Product.class,id);

        assertThat(product1.getCost()).isEqualTo(500);
    }

    @Test
    public void delete(){
        Integer id = 2;
        productRepository.deleteById(id);
        Optional<Product> result = productRepository.findById(id);


        assertThat(!result.isPresent());
    }

    @Test
    public void testSaveProductWithDetails(){

        Integer id = 1;

        Product product = productRepository.findById(id).get();

        product.addDetail("device memory","128 GB");
        product.addDetail("cpu model","abc");
        product.addDetail("os","android ");


        Product product1 = productRepository.save(product);

        assertThat(product1.getDetails()).isNotEmpty();
    }

}
