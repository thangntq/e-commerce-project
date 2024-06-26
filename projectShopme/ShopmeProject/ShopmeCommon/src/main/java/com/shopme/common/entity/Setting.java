package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "settings")
public class Setting {

    @Id
    @Column(nullable = false,length = 128,name = "`key`")
    private String key;


    @Column(nullable = false,length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(length = 45,nullable = false)
    private SettingCategory category;

    public Setting(String key, String value, SettingCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    public Setting() {
    }

    public Setting(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        if (key == null){
            if (setting.key != null){
                return false;
            }
        }else if (!key.equals(setting.key)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((key==null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", category=" + category +
                '}';
    }
}
