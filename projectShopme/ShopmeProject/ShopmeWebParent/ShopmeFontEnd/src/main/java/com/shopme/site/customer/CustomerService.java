package com.shopme.site.customer;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.setting.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }
    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);

        return customer == null;
    }
}
