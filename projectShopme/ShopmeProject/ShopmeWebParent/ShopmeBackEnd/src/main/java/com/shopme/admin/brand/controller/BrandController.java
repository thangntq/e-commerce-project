package com.shopme.admin.brand.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.category.Service.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;
    @GetMapping("")
    public String listAll(Model model){
        return listByPage(1,model,"id","asc",null);
    }

    @GetMapping("/new")
    public String newBrand(Model model){
        List<Category> listCategories =  categoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategories",listCategories);
        model.addAttribute("brand",new Brand());
        model.addAttribute("pageTitle","Create New Brand");

        return "brands/brand_form";
    }
    @PostMapping("/new")
    public String saveBrand(Brand brand, @RequestParam("fileImage")MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            Brand saveBrand = brandService.save(brand);

            String uploadDir = "brand-logos/"+saveBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }else {
            brandService.save(brand);
        }
        redirectAttributes.addFlashAttribute("message","The brand has been saved successfully !");

        return "redirect:/brands";
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum")int pageNum, Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){

        Page<Brand> page = brandService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Brand> listBrands = page.getContent();

        long startCount = (pageNum-1) * BrandService.BRANDS_PER_PAGE + 1;
        long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("keyword",keyword);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("reverseSortDir",reverseSortDir);

        return "brands/brands";
    }

    @GetMapping("/edit/{id}")
    public String editBrand(@PathVariable("id") Integer id,Model model,RedirectAttributes redirectAttributes){

        try {
            Brand brand = brandService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand",brand);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("pageTitle","Edit Brand (ID: "+id + ")");

            return "brands/brand_form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());

            return "redirect:/brands";
        }
    }
    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable("id") Integer id, Model model,RedirectAttributes redirectAttributes){

        try {
            brandService.delete(id);
            String brandDir = "brand-logos/" + id;
            FileUploadUtil.cleanDir(brandDir);
            FileUploadUtil.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message","The Brand ID " + id + " has been deleted successfully !");
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/brands";
    }
}
