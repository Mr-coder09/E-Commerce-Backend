package com.example.Main.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.example.Main.DTO.ProductRequest;
import com.example.Main.DTO.ProductResponse;

public interface ProductService {

//	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    List<ProductResponse> getAllProducts(
            int page,
            int size,
            String sort,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );
	
//	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    ProductResponse getProductById(Long id);

    
    @PreAuthorize("hasRole('ADMIN')")
    ProductResponse createProduct(ProductRequest request);

    
    @PreAuthorize("hasRole('ADMIN')")
    ProductResponse updateProduct(Long id, ProductRequest request);

    
    @PreAuthorize("hasRole('ADMIN')")
    void deleteProduct(Long id);
}