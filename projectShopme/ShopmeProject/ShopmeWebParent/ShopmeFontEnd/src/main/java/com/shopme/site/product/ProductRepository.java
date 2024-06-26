package com.shopme.site.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends PagingAndSortingRepository<Product,Integer> {


    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) ORDER BY p.name")
    public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

    public Product findByAlias(String alias);

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.name LIKE CONCAT('%', ?1, '%') OR p.shortDescription LIKE CONCAT('%', ?1, '%') OR p.fullDescription LIKE CONCAT('%', ?1, '%'))")
    public Page<Product> search(String keyword,Pageable pageable);
}
