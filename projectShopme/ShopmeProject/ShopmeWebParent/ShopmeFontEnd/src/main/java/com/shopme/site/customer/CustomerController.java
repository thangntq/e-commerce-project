package com.shopme.site.customer;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/register")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public String showRegisterForm(Model model){

        List<Country> listAllCountries = customerService.listAllCountries();
        model.addAttribute("listCountries" ,listAllCountries);
        model.addAttribute("pageTitle" ,"Customer Registration");
        model.addAttribute("customer" ,new Customer());

        return "register/register_form";
    }




}
