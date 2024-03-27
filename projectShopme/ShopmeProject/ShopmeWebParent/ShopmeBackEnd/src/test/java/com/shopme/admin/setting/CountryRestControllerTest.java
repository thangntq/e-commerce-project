package com.shopme.admin.setting;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepository;

    @Test
    @WithMockUser(username = "nam@codejava.net",password = "java2020",roles = "ADMIN")
    public void testListCountries() throws Exception {

        String url = "/countries/list";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net",password = "something",roles = "ADMIN")
    public void testCreateCountry() throws Exception {

        String url = "/countries/save";
        String countryName = "Germany";
        String countryCode = "DE";
        Country country = new Country(countryName,countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(response);
        Optional<Country> findById = countryRepository.findById(countryId);


        Country saveCountry = findById.get();
        assertThat(saveCountry.getName()).isEqualTo(countryName);

    }


    @Test
    @WithMockUser(username = "nam@codejava.net",password = "something",roles = "ADMIN")
    public void testUpdateCountry() throws Exception {

        String url = "/countries/save";

        Integer countryId = 6;
        String countryName = "China";
        String countryCode = "CN";
        Country country = new Country(countryId,countryName,countryCode);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(countryId)));



        Optional<Country> findById = countryRepository.findById(countryId);
        assertThat(findById.isPresent());
        Country saveCountry = findById.get();
        assertThat(saveCountry.getName()).isEqualTo(countryName);

    }
    @Test
    @WithMockUser(username = "nam@codejava.net",password = "something",roles = "ADMIN")
    public void testDeleteCountry() throws Exception {

        Integer countryId = 7;
        String url = "/countries/delete/" + countryId;

        mockMvc.perform(get(url)).andExpect(status().isOk());

        Optional<Country> findById = countryRepository.findById(countryId);
        assertThat(findById).isNotPresent();
    }
}
