package com.example.Main.Service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.DTO.ProductRequest;
import com.example.Main.DTO.ProductResponse;
import com.example.Main.Entity.Category;
import com.example.Main.Entity.Product;
import com.example.Main.Entity.User;
import com.example.Main.Enum.Role;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Mapper.ProductMapper;
import com.example.Main.Repository.CategoryRepository;
import com.example.Main.Repository.ProductRepository;
import com.example.Main.Service.ProductService;
import com.example.Main.Specification.ProductSpecification;



@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ProductMapper productMapper;
	
	
	
	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	
//	// 🔒 MANUAL ENTITY → DTO MAPPER (IMPORTANT)
//    private ProductResponse mapToResponse(Product product) {
//
//        ProductResponse response = new ProductResponse();
//        response.setId(product.getId());
//        response.setName(product.getName());
//        response.setDescription(product.getDescription());
//        response.setPrice(product.getPrice());
//        response.setStockQuantity(product.getStockQuantity());
//        response.setCategoryName(product.getCategory().getName());
//
//        return response;
//    }
	
	
	
	  @Override
	    public List<ProductResponse> getAllProducts(
	            int page,
	            int size,
	            String sort,
	            Long categoryId,
	            BigDecimal minPrice,
	            BigDecimal maxPrice) {

	        Sort sortObj;

	        if (sort != null && sort.contains(",")) {
	            String[] parts = sort.split(",");
	            sortObj = parts[1].equalsIgnoreCase("desc")
	                    ? Sort.by(parts[0]).descending()
	                    : Sort.by(parts[0]).ascending();
	        } else {
	            sortObj = Sort.by("id").ascending();
	        }
	        Pageable pageable = PageRequest.of(page, size, sortObj);

	        Specification<Product> spec = Specification.allOf(
	                ProductSpecification.categoryId(categoryId),
	                ProductSpecification.minPrice(minPrice),
	                ProductSpecification.maxPrice(maxPrice)
	        );

	        return productRepository.findAll(spec, pageable)
	                .getContent()
	                .stream()
	                .map(productMapper::toResponse)   
	                .collect(Collectors.toList());
	    }

	
	
	
	
	
	
	
	
	

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IdNotFoundException("Product Not Found "));
        
        
        
        return productMapper.toResponse(product);
    }

    // ✅ CREATE PRODUCT
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() ->
                        new IdNotFoundException("Category not found"));

//        Product product = new Product();
//        product.setName(productRequest.getName());
//        product.setDescription(productRequest.getDescription());
//        product.setPrice(productRequest.getPrice());
//        product.setStockQuantity(productRequest.getStockQuantity());
//        product.setStatus(productRequest.getStatus());
//        product.setCategory(category);
//        product.setCreatedAt(LocalDateTime.now());
//        product.setUpdatedAt(LocalDateTime.now());
        

        if (productRepository.existsByName(productRequest.getName())) {
			
	 		
	 		throw new BadRequestException(" Duplicate Entry ");
	 		
	 		
		}
        
        
        Product product = productMapper.toEntity(productRequest);
        product.setCategory(category);
        
        
        Product savedProduct = productRepository.save(product);
        
        ProductResponse response = productMapper.toResponse(savedProduct);
        
//        response.setCategoryName(savedProduct.getCategory().getName() );
        
//        response.setCategoryName(savedProduct.getCategory()).getName();
        
        log.info("Product saved: productId={}, name={}",
                savedProduct.getId(), savedProduct.getName());
        
        
        
        
        return response;
    }

    // ✅ UPDATE PRODUCT
    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IdNotFoundException("Product not found"));
        
        if (product.isDeleted()) {
            throw new IllegalStateException("Cannot update deleted product");
        }
        
        
        
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() ->
                        new IdNotFoundException("Category not found"));

        
        
//        product.setName(productRequest.getName());
//        product.setDescription(productRequest.getDescription());
//        product.setPrice(productRequest.getPrice());
//        product.setStockQuantity(productRequest.getStockQuantity());
//        product.setStatus(productRequest.getStatus());
//        product.setCategory(category);
//        product.setUpdatedAt(LocalDateTime.now());
        
//        Product product = productMapper.toEntity(productRequest);
        
        productMapper.updateEntity(productRequest, product, category);
        
        
        Product savedProduct = productRepository.save(product);
        
        ProductResponse response = productMapper.toResponse(savedProduct);
        
        
        log.info("Stock updated: productId={}, remainingStock={}",
                product.getId(), product.getStockQuantity());

        return response;
    }
    
    
    
    
    

    // ✅ DELETE PRODUCT
    @Transactional
    @Override
    
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IdNotFoundException("Product not found"));

        productRepository.delete(product);
        
    
		
	}
   
        
        
    }



