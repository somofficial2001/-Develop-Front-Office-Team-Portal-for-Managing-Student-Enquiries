package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "AIT_ENQUIRY_STATUS_ENTITY")
public class EnqStatusEntity {
 
	@Id
	@GeneratedValue
	private Integer id;
	private String courseStatus;
	
}
