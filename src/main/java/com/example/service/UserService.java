package com.example.service;

import com.example.binding.LoginForm;
import com.example.binding.SignupForm;
import com.example.binding.UnlockForm;

public interface UserService {

	public String login(LoginForm login);
	
	public boolean signUp(SignupForm signup);
	
	public boolean unlockAccount(UnlockForm unlock);
	
	public boolean ForgotPwd(String email);
}
