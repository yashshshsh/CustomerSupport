package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.service.interfac.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public Response createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/{id}")
    public Response getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping
    public Response getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/{id}")
    public Response updateCategory(
            @RequestBody Category category,
            @PathVariable Long id) {

        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}