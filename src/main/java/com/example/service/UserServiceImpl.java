package com.example.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.LoginForm;
import com.example.binding.SignupForm;
import com.example.binding.UnlockForm;
import com.example.entity.UserDtlsEntity;
import com.example.repo.UserDtlsRepo;
import com.example.utils.EmailUtils;
import com.example.utils.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private HttpSession session;

	@Override
	public String login(LoginForm form) {

		UserDtlsEntity entity = userDtlsRepo.findByEmailAndPwd(form.getEmail(), form.getPwd());

		if (entity == null) {
			return "Invalid Credential";
		}
		if (entity.getAccountStatus().equals("LOCKED")) {
			return "Your Account Locked...";
		}

		// create session store user data
		
		session.setAttribute("userId", entity.getUserId());

		return "Successs";
	}

	@Override
	public boolean signUp(SignupForm signup) {

		UserDtlsEntity user = userDtlsRepo.findByEmail(signup.getEmail());

		if (user != null) {
			return false;
		}

		// copy data from binding object to entity obj

		UserDtlsEntity entity = new UserDtlsEntity();
		BeanUtils.copyProperties(signup, entity);

		// TODO : create random password

		String tempPwd = PwdUtils.generateRandomPwd();
		entity.setPwd(tempPwd);

		// TODO : set Account Status as locked

		entity.setAccountStatus("Locked");

		// TODO : Insert Record

		userDtlsRepo.save(entity);

		// TODO :Send email to unlock the account

		String to = signup.getEmail();
		String subject = "Unlock Your account";

		StringBuffer body = new StringBuffer("");

		body.append("<h1>Use below temporary password to Unlock your Account<h1>");

		body.append("Temporary Password :" + tempPwd);

		body.append("<br/>");

		body.append("<a href=\"http://localhost:8080/unlock?email=" + to + "\">click here to Unlock Your Account</a>");

		emailUtils.sendEmail(to, subject, body.toString());
		return true;
	}

	@Override
	public boolean unlockAccount(UnlockForm form) {

		UserDtlsEntity entity = userDtlsRepo.findByEmail(form.getEmail());

		if (entity.getPwd().equals(form.getTempPwd())) {

			entity.setPwd(form.getNewPwd());
			entity.setAccountStatus("UNLOCKED");
			userDtlsRepo.save(entity);
			return true;

		} else {
			return false;
		}

	}

	@Override
	public boolean ForgotPwd(String email) {

		// check record presense in db with given email
		UserDtlsEntity entity = userDtlsRepo.findByEmail(email);

		// if record not available return false
		if (entity == null) {
			return false;
		}

		// if record available send pwd to email and send success msg.
		String Subject = "Recover PassWord";
		String body = "Your Password :  " + entity.getPwd();

		emailUtils.sendEmail(email, Subject, body);

		return true;
	}

}
