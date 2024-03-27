package com.shopme.site;

import com.shopme.common.entity.Category;
import com.shopme.site.category.CategoryRepositories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepositories repository;

    @Test
    public void testListEnabledCategories(){
        List<Category> categories = repository.findAllEnabled();

        categories.forEach(category -> {
            System.out.println(category.getName() + " ("+category.isEnabled() + ")");
        });
    }
    @Test
    public void testFindCategoryByAlias(){
        String alias = "camera";

        Category category = repository.findByAliasEnabled(alias);

        assertThat(category).isNotNull();

    }
}
