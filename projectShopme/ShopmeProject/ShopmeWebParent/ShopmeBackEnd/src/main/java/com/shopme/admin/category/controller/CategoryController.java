package com.shopme.admin.category.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryPageInfo;
import com.shopme.admin.category.Service.CategoryService;

import com.shopme.admin.category.exporter.CategoryCsvExporter;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String listFirstPage(@Param("sortDir")String sortDir , Model model){
        return listByPage(1,sortDir,model,null);
    }
    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir")String sortDir,Model model,@Param("keyword")String keyword){
        if (sortDir == null || sortDir.isEmpty()){
            sortDir= "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listCategories = categoryService.listByPage(pageInfo,pageNum,sortDir,keyword);
        long startCount = (pageNum-1)* CategoryService.ROOT_CATEGORIES_PER_PAGE +1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE -1;
        if (endCount > pageInfo.getTotalElements()){
            endCount = pageInfo.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("totalPages",pageInfo.getTotalPage());
        model.addAttribute("totalItems",pageInfo.getTotalElements());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("sortField","name");
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("reverseSortDir",reverseSortDir);

        return "categories/categories";
    }


    @GetMapping("/new")
    public String newCategory(Model model){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("category",new Category());
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("pageTitle","Create New Category");
        return "categories/categories_form";
    }
    @PostMapping("/save")
    public String saveCategory(Category category, @RequestParam("fileImage")MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        if(!multipartFile.isEmpty()) {


            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message","The category save has been saved successfully");
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable(name = "id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category",category);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("pageTitle","Edit Category (ID: " + id + ")");
            return "categories/categories_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/categories";
        }
    }
    @GetMapping("/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes){
        categoryService.updateCategoryEnabledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been "+ status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id,Model model,RedirectAttributes redirectAttributes){

        try {
            categoryService.delete(id);
            String categoryDir = "category-images/"+id;
            FileUploadUtil.cleanDir(categoryDir);
            FileUploadUtil.removeDir(categoryDir);
            redirectAttributes.addFlashAttribute("message","The category ID " + id + "has been delete successfully");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        CategoryCsvExporter exporter = new CategoryCsvExporter();

        exporter.export(listCategories,response);
    }
}
