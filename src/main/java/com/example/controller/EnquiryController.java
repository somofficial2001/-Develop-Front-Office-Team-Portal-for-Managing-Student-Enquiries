package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySerchCriteria;
import com.example.entity.StudentEnqEntity;
import com.example.service.EnquiryService;

@Controller
public class EnquiryController {
	
	@Autowired 
	private HttpSession session;
	
	@Autowired
	private EnquiryService  enqService;
	
	
	@GetMapping("/edit")
	public String editEnq(@RequestParam ("enquiryId") Integer enquiryId,Model model) {
		
		initForm(model);
		StudentEnqEntity edit = enqService.editEnquiry(enquiryId);
		model.addAttribute("formObj", edit);
		
		return"add-enquiry";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return"index";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
	Integer userId	= (Integer) session.getAttribute("userId");
		
		DashboardResponse dashboardData = enqService.getDashboardData(userId);
		
		model.addAttribute("dashboardData", dashboardData);
		
		return "dashboard";
	}
	@GetMapping("/addEnq")
	public String initForm(Model model) {		
		
		List<String> courses = enqService.getCourses();		
		
		List<String> enqStatus = enqService.getEnqStatus();		
		
		EnquiryForm formObj = new EnquiryForm();
		
		model.addAttribute("courseNames", courses);
		model.addAttribute("StatusNames", enqStatus);
		model.addAttribute("formObj", formObj);
		
		return "add-enquiry";
	}
	
	@PostMapping("/addEnq")
	public String addEnqiry(@ModelAttribute("formObj") EnquiryForm formObj, Model model) {
		
		System.out.println(formObj);
		
		//TODO : save the Data
		
		boolean status = enqService.saveEnquiry(formObj);
		
		if (status) {
			model.addAttribute("succMsg", "Enquiry Added");
		} else {
			model.addAttribute("errMsg", "problem occured");

		}
		return "add-enquiry";
	}
	
	@GetMapping("/view-enquiries")
	public String viewEnqiries(Model model) {
		
		initForm(model);
		model.addAttribute("searchForm", new EnquirySerchCriteria());
		 List<StudentEnqEntity> enquiries = enqService.getEnquiries();
		model.addAttribute("enquiries",enquiries);
		
		return "view-enquiries";
	}
	
	@GetMapping("/filter-enquiries")
	public String getFilterdEnqs(
			@RequestParam String cname ,
			@RequestParam String status,
			@RequestParam String mode,
			Model model) {
		
		EnquirySerchCriteria criteria = new EnquirySerchCriteria();
		criteria.setCourseName(cname);
		criteria.setClassMode(mode);
		criteria.setEnquiryStatus(status);
		
		Integer userId	= (Integer) session.getAttribute("userId");
		List<StudentEnqEntity> filteredEnqs = enqService.getFilteredEnqs(criteria, userId);

		model.addAttribute("enquiries",filteredEnqs);		
		return "filter-enquiries-page";
	}

}
