package com.plannerapp.web;

import com.plannerapp.config.LoggedUser;
import com.plannerapp.model.entity.Task;
import com.plannerapp.service.AuthService;
import com.plannerapp.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final AuthService authService;
    private final LoggedUser loggedUser;
    private final TaskService taskService;

    public HomeController(AuthService authService, LoggedUser loggedUser, TaskService taskService) {
        this.authService = authService;
        this.loggedUser = loggedUser;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String notLoggedInUser() {
        if (this.authService.isLoggedIn()) {
            return "redirect:/home";
        }
        return "index";
    }
    @GetMapping("/home")
    public String home(Model model){
        long loggedUserId = this.loggedUser.getId();
        if (!this.authService.isLoggedIn()) {
            return "redirect:/login";
        }

        List<Task> myTasks = this.taskService.findAllTaskByUserId(loggedUserId);
        List<Task> otherTasks = this.taskService.findAllTaskByUserIdNot(loggedUserId);
//        List<Task> notOwnedByNobody = this.taskService.findAllTaskByUserIsNull();

        model.addAttribute("username",loggedUser.getUsername());
        model.addAttribute("myTasks",myTasks);
        model.addAttribute("otherTasks",otherTasks);

        return "home";
    }


}
