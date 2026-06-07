package com.example.Main.HelperClass;

import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@FilterDef(
    name = "softDeleteFilter",
    parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(
    name = "softDeleteFilter",
    condition = "is_deleted = :isDeleted"
)

@Data
public abstract class BaseSoftDeleteEntity {

	@Column(nullable = false)
    protected boolean isDeleted = false;

    protected LocalDateTime deletedAt;
	
}
