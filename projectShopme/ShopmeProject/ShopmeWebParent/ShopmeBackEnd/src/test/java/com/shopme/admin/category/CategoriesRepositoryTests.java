package com.shopme.admin.category;


import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoriesRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    @Test
    public void testListRootCategories(){

        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());

        rootCategories.forEach(category -> System.out.println(category.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computers";
        Category category = categoryRepository.findByName(name);

        assertThat(category).isNotNull();
        System.out.println(category.getAlias());
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String name = "Electronics";
        Category category = categoryRepository.findByAlias(name);

        assertThat(category).isNotNull();
        System.out.println(category.getAlias());
        assertThat(category.getAlias()).isEqualTo(name);
    }
}
