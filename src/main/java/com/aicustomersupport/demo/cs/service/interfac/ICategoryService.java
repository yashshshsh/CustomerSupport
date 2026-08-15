package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Category;

public interface ICategoryService {

    Response createCategory(Category category);

    Response getCategory(Long id);

    Response getAllCategories();

    Response updateCategory(Category category, Long id);

    Response deleteCategory(Long id);
}