package com.shopme.site.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> listAllSettings(){

        return (List<Setting>) settingRepository.findAll();
    }

    public List<Setting> getGeneralSettings(){
        List<Setting> settings = new ArrayList<>();
        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return settings;
    }

}
