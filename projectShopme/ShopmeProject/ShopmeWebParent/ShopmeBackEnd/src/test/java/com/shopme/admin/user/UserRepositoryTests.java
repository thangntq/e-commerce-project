package com.shopme.admin.user;

import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void testCreateUser(){
        Role roleADmin = entityManager.find(Role.class,1);
        User user = new User("thanhthuy89@gmail.com","thuy2003","thuy","dan");
        user.addRole(roleADmin);

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateNewUserWithTwoRoles(){
        User user = new User("abc@gmail.com","123456","a","b");
        Role roleEditor = new Role(2);
        Role roleAssitant = new Role(1);
        user.addRole(roleEditor);
        user.addRole(roleAssitant);

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testListAllUsers(){
        Iterable<User> userList = userRepository.findAll();

        userList.forEach(user -> System.out.println(user));

    }
    @Test
    public void testGetUserById(){
        User user = userRepository.findById(2).get();
        System.out.println(user);
        assertThat(user).isNotNull();
    }
    @Test
    public void testUpdateUserDetails(){
        User user = userRepository.findById(2).get();
        user.setEnabled(true);
        user.setPhotos("nguyen the quang thang");


        userRepository.save(user);
        System.out.println(user);
    }
    @Test
    public void testUpdateUserRole(){
        User user = userRepository.findById(6).get();
        Role roleEditor = new Role(3);
        Role roleAdmin = new Role(1);
        user.getRoles().remove(roleEditor);
        user.addRole(roleAdmin);
        userRepository.save(user);
        System.out.println(user);
    }
    @Test
    public void testDeleUser(){
        Integer userId = 7;
        userRepository.deleteById(userId);

        Iterable<User> userList = userRepository.findAll();

        userList.forEach(user -> System.out.println(user));
    }
    @Test
    public void testGetUSerByEmail(){
        String email = "abc@gmail.com";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }
    @Test
    public void testCountById(){
        Integer id = 1;
        Long countById = userRepository.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }
    @Test
    public void testDisnableUser(){
        Integer id = 6;
        userRepository.updateEnabledStatus(id,false);
    }
    @Test
    public void testListFirstPage(){
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }
    @Test
    public void testSearchUsers(){
        String keyword = "bruce";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(keyword,pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
