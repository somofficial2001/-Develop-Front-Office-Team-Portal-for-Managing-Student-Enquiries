package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.binding.LoginForm;
import com.example.binding.SignupForm;
import com.example.binding.UnlockForm;
import com.example.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/forgotPwd")
	public String forgotPassword(Model model) {
		
		
		return "forgotPwd";
	}
	
	@PostMapping("/forgotPwd")
	public String forgot(@RequestParam ("email") String email,Model  model) {
		
		System.out.println(email);
		
		boolean status = userService.ForgotPwd(email);
		
		if(status) {
			//send success msg
			model.addAttribute("succMsg","Pwd sent to your Email");
		}else{
			//send error msg
			model.addAttribute("errMsg","Invalid Email");
		}
		
		return"forgotPwd";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {

		model.addAttribute("loginForm", new LoginForm());

		return "login";
	}

	@PostMapping("/login") 
	public String login(@ModelAttribute("loginForm") LoginForm loginForm, Model model) {
		System.out.println(loginForm);

		String status = userService.login(loginForm);

		if (status.contains("Successs")) {
			// Display Dashboard

			return "redirect:/dashboard";
		}

		model.addAttribute("errMsg", status);

		return "login";
	}
	
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("user", new SignupForm());
		return "signup";
	}

	@PostMapping("/signup")
	public String handleSignUp(@ModelAttribute("user") SignupForm form, Model model) {

		boolean status = userService.signUp(form);

		if (status) {
			model.addAttribute("succMsg", "Account created ,Check Your Email");
		} else {
			model.addAttribute("errMsg", "Email already Exist");
		}
		return "signup";
	}

	@GetMapping("/unlock")
	public String unlock(@RequestParam String email, Model model) {
		UnlockForm unlockFormObj = new UnlockForm();
		unlockFormObj.setEmail(email);

		model.addAttribute("unlock", unlockFormObj);
		return "unlock";
	}

	@PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute("unlock") UnlockForm unlock, Model model) {

		System.out.println(unlock);

		if (unlock.getNewPwd().equals(unlock.getConfirmPwd())) {

			boolean status = userService.unlockAccount(unlock);
			if (status) {
				model.addAttribute("succMsg", "Your Account Unlocked Succesfully... ");
			} else {
				model.addAttribute("errMsg", "Given temporary password is incorrect ,please check your email");
			}

		} else {
			model.addAttribute("errMsg", "new passward And Confirm password Should bie same");
		}

		return "unlock";
	}

	

	

}
