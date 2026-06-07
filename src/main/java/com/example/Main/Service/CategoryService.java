package com.example.Main.Service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.example.Main.DTO.CategoryRequest;
import com.example.Main.DTO.CategoryResponse;


public interface CategoryService {
	
	
	
	
	@PreAuthorize("permitAll()")
    List<CategoryResponse> getAllCategories();

    
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    CategoryResponse getCategoryById(Long id);

    @PreAuthorize("hasRole('ADMIN')")
    CategoryResponse createCategory(CategoryRequest categoryRequest);


    @PreAuthorize("hasRole('ADMIN')")
    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteCategory(Long id);

}
