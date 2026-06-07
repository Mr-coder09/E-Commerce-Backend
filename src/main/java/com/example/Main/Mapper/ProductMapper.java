package com.example.Main.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.example.Main.DTO.ProductRequest;
import com.example.Main.DTO.ProductResponse;
import com.example.Main.Entity.Category;
import com.example.Main.Entity.Product;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		imports = LocalDateTime.class
		)
public interface ProductMapper {
	
	


	@Mapping(target = "category", source = "category")

    @Mapping(target = "id", ignore = true)  
    @Mapping(target = "name", source = "productRequest.name")
    @Mapping(target = "description", source = "productRequest.description")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
	    void updateEntity(ProductRequest productRequest,
	                      @MappingTarget Product product,
	                      Category category);
	
	@Mapping(target = "updatedAt" , expression = "java(LocalDateTime.now())")
	@Mapping(target = "categoryName" , source = "category.name")
	ProductResponse toResponse(Product product);
	
	@Mapping(target = "id" , ignore = true)
	@Mapping(target = "createdAt" ,expression = "java(LocalDateTime.now())")
	@Mapping(target = "updatedAt" ,expression = "java(LocalDateTime.now())")
	Product toEntity(ProductRequest productRequest);

}
