package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryRestController {
    @Autowired private CountryRepository repository;


    @GetMapping("/countries/list")
    public List<Country> listAll(){

        return repository.findAllByOrderByNameAsc();
    }

    @PostMapping("/countries/save")
    public String save(@RequestBody Country country){
        Country saveCountry = repository.save(country);
        return String.valueOf(saveCountry.getId());
    }

    @DeleteMapping("/countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        repository.deleteById(id);
    }
}
