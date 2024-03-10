package com.shopme.admin.user.service;


import com.shopme.admin.user.handleException.UserNotFoundException;
import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class UserService {
    public static final int USER_PER_PAGE = 5;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public  User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }
    public List<User> listAll(){
        return (List<User>) userRepository.findAll();
    }
    public List<Role> listRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public User updateAccount(User userInform){
        User userInDB = userRepository.findById(userInform.getId()).get();

        if (!userInform.getPassword().isEmpty()){
                userInDB.setPassword(userInform.getPassword());
                encodePassword(userInDB);
        }
        if (userInform.getPhotos()!=null){
            userInDB.setPhotos(userInform.getPhotos());
        }
        userInDB.setFirstName(userInform.getFirstName());
        userInDB.setLastName(userInform.getLastName());

        return userRepository.save(userInDB);
    }

    private void encodePassword(User user){
        String encodeedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodeedPassword);
    }

    public boolean isEmailUnique(Integer id,String email){
       User userByEmail = userRepository.getUserByEmail(email);

       if (Objects.isNull(userByEmail))return true;

       boolean isCreatingNew = (id == null);

       if (isCreatingNew){
           if (userByEmail != null) return false;
       }else {
           if (userByEmail.getId() != id){
               return false;
           }
       }
       return true;
    }
    public Page<User> listByPage(int pageNumber,String sortField,String sortDir,String keyword){
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();


        Pageable pageable = PageRequest.of(pageNumber-1,USER_PER_PAGE,sort);
        if (keyword != null){
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }
    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundException("Cound not find any user with id: "+id);
        }
    }
    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with Id: "+id);
        }
        userRepository.deleteById(id);
    }
    public void updateUserEnabledStatus(Integer id,boolean enabled){
        userRepository.updateEnabledStatus(id,enabled);
    }
}
