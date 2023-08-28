package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySerchCriteria;
import com.example.entity.CourseEntity;
import com.example.entity.EnqStatusEntity;
import com.example.entity.StudentEnqEntity;
import com.example.entity.UserDtlsEntity;
import com.example.repo.CourseRepo;
import com.example.repo.EnqStatusRepo;
import com.example.repo.StudenEnquiryRepo;
import com.example.repo.UserDtlsRepo;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private StudenEnquiryRepo enqRepo;

	@Autowired
	private CourseRepo coursesRepo;

	@Autowired
	private EnqStatusRepo statusRepo;

	@Autowired
	private HttpSession session;

	@Override
	public DashboardResponse getDashboardData(Integer userId) {

		DashboardResponse response = new DashboardResponse();

		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);

		if (findById.isPresent()) {
			UserDtlsEntity userEntity = findById.get();
			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();

			Integer totalCnt = enquiries.size();

			Integer enrolledCnt = enquiries.stream().filter(e -> e.getEnquiryStatus().equals("Enrolled"))
					.collect(Collectors.toList()).size();

			Integer lostCnt = enquiries.stream().filter(e -> e.getEnquiryStatus().equals("Lost"))
					.collect(Collectors.toList()).size();

			response.setTotalEnqcnt(totalCnt);
			response.setEnrollCnt(enrolledCnt);
			response.setLostCnt(lostCnt);

		}

		return response;
	}

	@Override
	public List<String> getCourses() {

		List<CourseEntity> findAll = coursesRepo.findAll();
		List<String> names = new ArrayList<>();

		for (CourseEntity entity : findAll) {
			names.add(entity.getCourseName());

		}

		return names;
	}

	@Override
	public List<String> getEnqStatus() {

		List<EnqStatusEntity> findAll = statusRepo.findAll();
		List<String> statusList = new ArrayList<>();

		for (EnqStatusEntity entity : findAll) {
			statusList.add(entity.getCourseStatus());
		}

		return statusList;
	}

	@Override
	public boolean saveEnquiry(EnquiryForm form) {

		StudentEnqEntity enqEntity = new StudentEnqEntity();
		BeanUtils.copyProperties(form, enqEntity);

		Integer userId = (Integer) session.getAttribute("userId");

		UserDtlsEntity userEntity = userDtlsRepo.findById(userId).get();
		enqEntity.setUserEntity(userEntity);
		enqRepo.save(enqEntity);

		return true;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries() {

		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);

		if (findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			return enquiries;
		}

		return null;
	}

	@Override
	public List<StudentEnqEntity> getFilteredEnqs(EnquirySerchCriteria criteria, Integer userId) {
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);

		if (findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();

			// filter Logic

			if (null != criteria.getCourseName() && !"".equals(criteria.getCourseName())) {

				enquiries = enquiries.stream().filter(e -> e.getCourseName().equals(criteria.getCourseName()))
						.collect(Collectors.toList());

			}

			if (null != criteria.getEnquiryStatus() && !"".equals(criteria.getEnquiryStatus())) {

				enquiries = enquiries.stream().filter(e -> e.getEnquiryStatus().equals(criteria.getEnquiryStatus()))
						.collect(Collectors.toList());

			}
			if (null != criteria.getClassMode() && !"".equals(criteria.getClassMode())) {

				enquiries = enquiries.stream().filter(e -> e.getClassMode().equals(criteria.getClassMode()))
						.collect(Collectors.toList());

			}
			return enquiries;
		}

		return null;
	}
	@Override
	public StudentEnqEntity editEnquiry(Integer enquiryId) {
	 	
			Optional<StudentEnqEntity> findById = enqRepo.findById(enquiryId);
			if (findById.isPresent()) {
				StudentEnqEntity studentEnqEntity = findById.get();
				return studentEnqEntity;
			}
		return null;
	}

}
