package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTest {
    @Autowired
    private SettingRepository settingRepository;

    @Test
    public void testCreateGeneralSetting(){
        Setting mailFrom = new Setting("MAIL_FROM", "shopme@gmail.com", SettingCategory.MAIL_SEVER);

        settingRepository.delete(mailFrom);
    }
    @Test
    public void testCreateCurrencySettings() {
        Setting mailFrom = new Setting("CUSTOMER_VERIFY_CONTENT", "email content", SettingCategory.MAIL_TEMPLATES);

        settingRepository.save(mailFrom);

    }
}
