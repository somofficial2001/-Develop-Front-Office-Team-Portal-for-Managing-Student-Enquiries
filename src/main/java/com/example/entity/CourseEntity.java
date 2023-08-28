package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "AIT_COURSE_ENTITY")
public class CourseEntity {

	@Id
	@GeneratedValue
	private Integer id;
	private String courseName;

}
