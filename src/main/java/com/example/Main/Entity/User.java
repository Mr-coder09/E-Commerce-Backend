package com.example.Main.Entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.Main.Enum.Role;
import com.example.Main.HelperClass.BaseSoftDeleteEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)



@SQLDelete(sql = " UPDATE users SET is_deleted = true  WHERE id =? ")


@Data
public class User  extends BaseSoftDeleteEntity {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

private String name;

@Column(unique = true)
private String email;

private String password;

private String gender;

@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Role role;


@Column(unique = true)
private String mobileNo;




@CreatedBy
@Column(updatable = false)
private String createdBy;

@LastModifiedBy
private String updatedBy;


@CreatedDate
@Column(updatable = false)
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;


}
