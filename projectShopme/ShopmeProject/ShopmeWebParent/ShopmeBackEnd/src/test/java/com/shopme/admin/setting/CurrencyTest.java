package com.shopme.admin.setting;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private SettingRepository settingRepository;
    @Test
    public void testCreateCurrency(){
        List<Currency> listCurrencies = Arrays.asList(
                new Currency("United States dollar","$","USD"),
                new Currency("British pound","£","GPB"),
                new Currency("Japanese","¥","JPY"),
                new Currency("Euro","€","EUR"),
                new Currency("Russian Ruble","₽","RUB"),
                new Currency("South Korean Won","₩","KRW"),
                new Currency("Chinese Yuan","¥","CNY"),
                new Currency("Brazilian Real","$","BRL"),
                new Currency("Australian Dollar","₩","AUD"),
                new Currency("Canadian Dollar","$","CAD"),
                new Currency("Vietnamese đồng","đ","VND"),
                new Currency("Indian Rupee","₹","INR")
                );

        currencyRepository.saveAll(listCurrencies);

        Iterable<Currency> iterable = currencyRepository.findAll();

        assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testListAllOrderByNameAsc(){
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();

        currencies.forEach(System.out::println);

        assertThat(currencies).size().isGreaterThan(0);
    }

    @Test
    public void testListSettingByCategory(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);

        settings.forEach(System.out::println);
    }
}
