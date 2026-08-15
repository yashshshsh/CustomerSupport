package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.service.interfac.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Response createCategory(Category category) {

        Response response = new Response();

        try {

            if (categoryRepository.existsByName(category.getName())) {

                response.setStatusCode(400);
                response.setMessage("Category already exists");

                return response;
            }

            Category savedCategory = categoryRepository.save(category);

            response.setStatusCode(200);
            response.setMessage("Category created successfully");
            response.setCategory(savedCategory);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating category: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getCategory(Long id) {

        Response response = new Response();

        try {

            Optional<Category> categoryOptional =
                    categoryRepository.findById(id);

            if (categoryOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Category not found");

                return response;
            }

            response.setStatusCode(200);
            response.setCategory(categoryOptional.get());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting category: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAllCategories() {

        Response response = new Response();

        try {

            List<Category> categories = categoryRepository.findAll();

            response.setStatusCode(200);
            response.setMessage("Categories retrieved successfully");
            response.setCategories(categories);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting categories: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response updateCategory(Category category, Long id) {

        Response response = new Response();

        try {

            Optional<Category> categoryOptional =
                    categoryRepository.findById(id);

            if (categoryOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Category not found");

                return response;
            }

            Category existingCategory = categoryOptional.get();

            if (category.getName() != null) {
                existingCategory.setName(category.getName());
            }

            if (category.getDescription() != null) {
                existingCategory.setDescription(category.getDescription());
            }

            Category updatedCategory =
                    categoryRepository.save(existingCategory);

            response.setStatusCode(200);
            response.setMessage("Category updated successfully");
            response.setCategory(updatedCategory);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while updating category: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response deleteCategory(Long id) {

        Response response = new Response();

        try {

            if (!categoryRepository.existsById(id)) {

                response.setStatusCode(400);
                response.setMessage("Category not found");

                return response;
            }

            categoryRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage("Category deleted successfully");

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting category: " + e.getMessage()
            );
        }

        return response;
    }
}