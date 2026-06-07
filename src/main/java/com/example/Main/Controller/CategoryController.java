package com.example.Main.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.CategoryRequest;
import com.example.Main.DTO.CategoryResponse;
import com.example.Main.Entity.Category;
import com.example.Main.Service.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	// 1️⃣ Get all categories
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryResponse>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	// 2️⃣ Get category by ID
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryService.getCategoryById(id));
	}

	// 3️⃣ Create category (ADMIN)
	@PostMapping("/admin/categories")
	public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {

		CategoryResponse response = categoryService.createCategory(categoryRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 4️⃣ Update category (ADMIN)
	@PutMapping("/admin/categories/{id}")
	public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
			@RequestBody CategoryRequest categoryRequest) {

		return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest));
	}

	// 2. Get category by ID

	// 5️⃣ Delete category (ADMIN)
	@DeleteMapping("/admin/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build(); // 204
	}

}
