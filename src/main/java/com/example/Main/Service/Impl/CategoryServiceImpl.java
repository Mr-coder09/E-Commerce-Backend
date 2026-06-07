package com.example.Main.Service.Impl;

//import java.time.LocalDateTime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.Main.DTO.CategoryRequest;
import com.example.Main.DTO.CategoryResponse;

import com.example.Main.Entity.Category;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Mapper.CategoryMapper;
import com.example.Main.Repository.CategoryRepository;
import com.example.Main.Service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	CategoryMapper categoryMapper;
	
	
	
	
//	private CategoryResponse mapToResponse(Category category) {
//
//	    CategoryResponse response = new CategoryResponse();
//	    response.setId(category.getId());
//	    response.setName(category.getName());
//	    response.setDescription(category.getDescription());
//
//	    return response;
//	}
//	

	
	
	@Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

	
	
	

	
	 @Override
	    public CategoryResponse getCategoryById(Long id) {
	        Category category = categoryRepository.findById(id)
	                .orElseThrow(() -> new IdNotFoundException("Category not found"));
	        return categoryMapper.toResponse(category);
	    }
	
	

	 
	 
	 @Override
	    public CategoryResponse createCategory(CategoryRequest request) {
//	        Category category = new Category();
//	        category.setName(request.getName());
//	        category.setDescription(request.getDescription());
//	        category.setCreatedAt(LocalDateTime.now());
//	        category.setUpdatedAt(LocalDateTime.now());
	        
		 	
		 	if (categoryRepository.existsByName(request.getName())) {
				
		 		
		 		throw new BadRequestException(" Duplicate Entry ");
		 		
		 		
			}
	        
		 
	        Category category = categoryMapper.toEntity(request);
	        
	        

	        return categoryMapper.toResponse(categoryRepository.save(category));
	    }
	
	
	
	
	 @Override
	    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
	        Category category = categoryRepository.findById(id)
	                .orElseThrow(() -> new IdNotFoundException("Category not found"));

//	        category.setName(request.getName());
//	        category.setDescription(request.getDescription());
//	        category.setUpdatedAt(LocalDateTime.now());
	        
	        
	        categoryMapper.updateEntity(request, category);
	        
	       
	        
	        
	        
	        return categoryMapper.toResponse(  categoryRepository.save(category));
	    }

	
	
	
	
	@Override
	public void deleteCategory(Long id) {
//		categoryRepository.deleteById(id);
		if (!categoryRepository.existsById(id)) {
		    throw new IdNotFoundException("Category not found");
		}
		categoryRepository.deleteById(id);
		
	}

	
}
