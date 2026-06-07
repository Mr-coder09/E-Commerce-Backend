package com.example.Main.Mapper;



import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.example.Main.DTO.CategoryRequest;
import com.example.Main.DTO.CategoryResponse;
import com.example.Main.Entity.Category;

@Mapper(
		componentModel = "spring" ,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		 imports = LocalDateTime.class
		)



public interface CategoryMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true) // ❗ never change
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
	void updateEntity(CategoryRequest request , @MappingTarget Category category);
	
	@Mapping(target = "id" , ignore = true )
	@Mapping(target = "createdAt" ,expression = "java(LocalDateTime.now())")
	@Mapping(target = "updatedAt" ,expression = "java(LocalDateTime.now())")
	Category toEntity(CategoryRequest request);
	
	
	CategoryResponse toResponse (Category category);
}
