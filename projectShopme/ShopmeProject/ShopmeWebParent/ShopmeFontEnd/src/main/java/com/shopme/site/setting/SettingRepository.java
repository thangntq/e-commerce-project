package com.shopme.site.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SettingRepository extends CrudRepository<Setting,String> {


    public List<Setting> findByCategory(SettingCategory category);


    @Query("SELECT s from  Setting s WHERE s.category = ?1 or s.category = ?2")
    public List<Setting> findByTwoCategories(SettingCategory catOne, SettingCategory catTwo);
}
