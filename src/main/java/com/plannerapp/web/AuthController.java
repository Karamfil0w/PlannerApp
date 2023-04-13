package com.plannerapp.web;

import com.plannerapp.model.dtos.LoginDto;
import com.plannerapp.model.dtos.RegisterDto;
import com.plannerapp.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @ModelAttribute("registerDto")
    public RegisterDto initRegisterDto(){
        return new RegisterDto();
    }
    @ModelAttribute("loginDto")
    public LoginDto initLoginDto() {
        return new LoginDto();
    }

    @GetMapping("/register")
    public String register() {
        if (this.authService.isLoggedIn()){
            return "redirect:/home";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto registerDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        if (this.authService.isLoggedIn()){
            return "redirect:/home";
        }
        if (bindingResult.hasErrors() || !this.authService.register(registerDto)) {

            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registerDto", bindingResult);
            return "redirect:/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        if (!this.authService.isLoggedIn()) {
            return "login";
        }
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("loginDto", loginDto);
            redirectAttributes.addFlashAttribute
                    ("org.springframework.validation.BindingResult.loginDto", bindingResult);
            return "redirect:/login";
        }

        if (!authService.login(loginDto)) {
            redirectAttributes.addFlashAttribute("loginDto", loginDto);
            redirectAttributes.addFlashAttribute("badCredentials", true);
            return "redirect:/login";
        }
        return "redirect:/home";
    }
    @GetMapping("/logout")
    public String logout() {
        if (this.authService.isLoggedIn()) {
            this.authService.logout();
            return "redirect:/";
        }
        return "redirect:/";
    }
}
