package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.*;

import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin","manage everything");
        Role saveRole = roleRepository.save(roleAdmin);
        assertThat(saveRole.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateRestRole(){
        Role roleSaleperson = new Role("SalePerson",
                "manage product price, customers, shipping, orders and sales reports");

        Role roleEditor = new Role("editor",
                "manage cagories, brands, products, articles and menu");

        Role roleShipper = new Role("Shipper","view products,view orders" +
                "and update order status");

        Role roleAssistant = new Role("Assitstant","manage questions and reviews");

        roleRepository.saveAll(List.of(roleAssistant,roleEditor,roleSaleperson,roleAssistant));
    }
}
