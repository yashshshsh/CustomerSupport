package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.CategoryDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.service.interfac.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Response createCategory(Category category) {
        try {
            if (categoryRepository.existsByName(category.getName())) {
                return Response.builder()
                        .statusCode(400)
                        .message("Category name already exists")
                        .build();
            }

            Category savedCategory = categoryRepository.save(category);
            return Response.builder()
                    .statusCode(200)
                    .message("Category created successfully")
                    .category(convertToDto(savedCategory))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error while creating category: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getCategory(Long id) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(id);
            if (categoryOpt.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Category retrieved successfully")
                        .category(convertToDto(categoryOpt.get()))
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message("Category not found with id: " + id)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error while retrieving category: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDto> categoryDtos = categories.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("Categories retrieved successfully")
                    .categories(categoryDtos)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error while getting categories: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response updateCategory(Category category, Long id) {
        try {
            Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
            if (existingCategoryOpt.isPresent()) {
                Category existingCategory = existingCategoryOpt.get();

                if (category.getName() != null) existingCategory.setName(category.getName());
                if (category.getDescription() != null) existingCategory.setDescription(category.getDescription());

                Category updatedCategory = categoryRepository.save(existingCategory);

                return Response.builder()
                        .statusCode(200)
                        .message("Category updated successfully")
                        .category(convertToDto(updatedCategory))
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message("Category not found with id: " + id)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error while updating category: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteCategory(Long id) {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                return Response.builder()
                        .statusCode(200)
                        .message("Category deleted successfully")
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message("Category not found")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error while deleting category: " + e.getMessage())
                    .build();
        }
    }

    private CategoryDto convertToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}