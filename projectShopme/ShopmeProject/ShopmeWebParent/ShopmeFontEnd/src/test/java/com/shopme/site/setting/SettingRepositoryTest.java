package com.shopme.site.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTest {
    
    @Autowired
    SettingRepository repository;
    
    @Test
    public void testFindByTwoCategories(){
        List<Setting> byTwoCategories = repository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);

        byTwoCategories.forEach(System.out::println);

    }
}
