package com.shopme.site.customer;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.site.setting.CountryRepository;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import net.bytebuddy.utility.RandomString;
@Service
@Transactional
public class CustomerService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    private CustomerRepository customerRepository;

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }
    public boolean isEmailUnique(String email){
        Customer customer = customerRepository.findByEmail(email);

        return customer == null;
    }

    public void registerCustomer(Customer customer){

        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());


        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());

        customer.setPassword(encodedPassword);
    }
    public boolean verify(String verificationCode) {
        Customer customer = customerRepository.findByVerificationCode(verificationCode);

        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepository.enable(customer.getId());
            return true;
        }
    }
}
