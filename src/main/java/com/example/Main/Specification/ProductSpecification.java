package com.example.Main.Specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.Main.Entity.Product;

public class ProductSpecification {

//	public static Specification<Product> categoryId(Long categoryId) {
//
//		return (root, query, cb) ->
//
//		categoryId == null ? cb.conjunction() : cb.equal(root.get("categoryId"), categoryId);
//
//	}
	
	public static Specification<Product> categoryId(Long categoryId) {
	    return (root, query, cb) ->
	        categoryId == null
	            ? cb.conjunction()
	            : cb.equal(root.get("category").get("id"), categoryId);
	}

	public static Specification<Product> minPrice(BigDecimal minPrice) {
	    return (root, query, cb) ->
	        minPrice == null
	            ? cb.conjunction()
	            : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
	}

	public static Specification<Product> maxPrice(BigDecimal maxPrice) {
	    return (root, query, cb) ->
	        maxPrice == null
	            ? cb.conjunction()
	            : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
	}

}
