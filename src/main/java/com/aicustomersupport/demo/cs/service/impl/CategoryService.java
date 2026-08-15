package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.CategoryDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.repository.TicketRepository;
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

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Response createCategory(Category category) {
        try {
            if (category == null ||
                    category.getName() == null ||
                    category.getName().isBlank()) {

                return Response.builder()
                        .statusCode(400)
                        .message("Category name is required")
                        .build();
            }

            String name = category.getName().trim();

            if (categoryRepository.existsByName(name)) {
                return Response.builder()
                        .statusCode(400)
                        .message("Category name already exists")
                        .build();
            }

            category.setName(name);

            Category savedCategory =
                    categoryRepository.save(category);

            return Response.builder()
                    .statusCode(201)
                    .message("Category created successfully")
                    .category(convertToDto(savedCategory))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error while creating category: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response getCategory(Long id) {
        try {
            Optional<Category> categoryOpt =
                    categoryRepository.findById(id);

            if (categoryOpt.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Category retrieved successfully")
                        .category(
                                convertToDto(categoryOpt.get())
                        )
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message(
                            "Category not found with id: " + id
                    )
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error while retrieving category: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response getAllCategories() {
        try {
            List<Category> categories =
                    categoryRepository.findAll();

            List<CategoryDto> categoryDtos =
                    categories.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message(
                            "Categories retrieved successfully"
                    )
                    .categories(categoryDtos)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error while getting categories: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response updateCategory(
            Category category,
            Long id) {

        try {
            if (category == null) {
                return Response.builder()
                        .statusCode(400)
                        .message("Category data is required")
                        .build();
            }

            Optional<Category> existingCategoryOpt =
                    categoryRepository.findById(id);

            if (existingCategoryOpt.isEmpty()) {
                return Response.builder()
                        .statusCode(404)
                        .message(
                                "Category not found with id: " + id
                        )
                        .build();
            }

            Category existingCategory =
                    existingCategoryOpt.get();

            if (category.getName() != null) {

                String newName =
                        category.getName().trim();

                if (newName.isBlank()) {
                    return Response.builder()
                            .statusCode(400)
                            .message(
                                    "Category name cannot be empty"
                            )
                            .build();
                }

                if (!newName.equalsIgnoreCase(
                        existingCategory.getName())) {

                    Optional<Category> categoryWithSameName =
                            categoryRepository.findByName(newName);

                    if (categoryWithSameName.isPresent() &&
                            !categoryWithSameName.get()
                                    .getId()
                                    .equals(id)) {

                        return Response.builder()
                                .statusCode(400)
                                .message(
                                        "Category name already exists"
                                )
                                .build();
                    }
                }

                existingCategory.setName(newName);
            }

            if (category.getDescription() != null) {
                existingCategory.setDescription(
                        category.getDescription()
                );
            }

            Category updatedCategory =
                    categoryRepository.save(existingCategory);

            return Response.builder()
                    .statusCode(200)
                    .message("Category updated successfully")
                    .category(
                            convertToDto(updatedCategory)
                    )
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error while updating category: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response deleteCategory(Long id) {

        try {
            Optional<Category> categoryOpt =
                    categoryRepository.findById(id);

            if (categoryOpt.isEmpty()) {
                return Response.builder()
                        .statusCode(404)
                        .message(
                                "Category not found with id: " + id
                        )
                        .build();
            }

            List<?> ticketsUsingCategory =
                    ticketRepository.findByCategoryId(id);

            if (!ticketsUsingCategory.isEmpty()) {
                return Response.builder()
                        .statusCode(409)
                        .message(
                                "Category cannot be deleted because it is referenced by existing tickets"
                        )
                        .build();
            }

            categoryRepository.delete(categoryOpt.get());

            return Response.builder()
                    .statusCode(200)
                    .message("Category deleted successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error while deleting category: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    private CategoryDto convertToDto(
            Category category) {

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}