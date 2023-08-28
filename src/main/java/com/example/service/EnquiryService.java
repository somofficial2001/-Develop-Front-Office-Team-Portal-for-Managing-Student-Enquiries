package com.example.service;

import java.util.List;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySerchCriteria;
import com.example.entity.StudentEnqEntity;


public interface EnquiryService {
	
	public List<String> getCourses();
	
	public List<String> getEnqStatus();
	
	public DashboardResponse getDashboardData(Integer userId);
	
	public boolean saveEnquiry(EnquiryForm form);
	
	public List<StudentEnqEntity> getEnquiries();
	
	public List <StudentEnqEntity> getFilteredEnqs(EnquirySerchCriteria criteria,Integer userId);
	
	public StudentEnqEntity editEnquiry(Integer enquiryId );
}
