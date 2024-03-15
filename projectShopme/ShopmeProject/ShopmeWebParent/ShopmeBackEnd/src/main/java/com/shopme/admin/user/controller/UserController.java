package com.shopme.admin.user.controller;


import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.exporter.UserCsvExporter;
import com.shopme.admin.user.exporter.UserExcelExporter;
import com.shopme.admin.user.exporter.UserPdfExporter;
import com.shopme.admin.user.handleException.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("")
    public String listFirstPage(Model model){

        return listByPage(1,model,"firstName","asc",null);
    }

    @GetMapping("/new")
    public String newUser(Model model){
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","Create New User");
        return "users/user_form";
    }
    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, Model model, @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,@Param("keyword") String keyword){
        Page<User> page = userService.listByPage(pageNum,sortField,sortDir,keyword);
        List<User> listUsers = page.getContent();

        long startCount = (pageNum-1)*UserService.USER_PER_PAGE +1;
        long endCount = startCount + userService.USER_PER_PAGE -1;
        if (endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("keyword",keyword);

        return "users/users";
    }
    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User saveUser = userService.save(user);

            String uploadDir = "user-photos/" + saveUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }
        redirectAttributes.addFlashAttribute("message","the user has been saved successfully!");
        return getRedirectURLtoAffectedUser(user);
    }

    private static String getRedirectURLtoAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id,Model model,RedirectAttributes redirectAttributes){
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit User (ID: " + id +")");
            model.addAttribute("listRoles",listRoles);
            return "users/user_form";
        }catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/users";
        }
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes){
        try {
            userService.delete(id);
            String userDir = "user-photos/"+id;
            FileUploadUtil.cleanDir(userDir);
            FileUploadUtil.removeDir(userDir);
            redirectAttributes.addFlashAttribute("message","The user Id " + id + " has been deleted successfully");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/users";
    }
    @GetMapping("/{id}/enabled/{status}")
    public String updateUserEnableStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){

        userService.updateUserEnabledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user Id " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message",message);

        return "redirect:/users";
    }

    @GetMapping("/export/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers,response);
    }
    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();

        UserExcelExporter excelExporter = new UserExcelExporter();
        excelExporter.export(listUsers,response);
    }

    @GetMapping("/export/pdf")
    public void exportPDF(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();

        UserPdfExporter excelExporter = new UserPdfExporter();
        excelExporter.export(listUsers,response);
    }
}
