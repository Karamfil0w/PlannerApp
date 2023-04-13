package com.plannerapp.web;

import com.plannerapp.config.LoggedUser;
import com.plannerapp.model.dtos.CreateTaskDto;
import com.plannerapp.service.AuthService;
import com.plannerapp.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final AuthService authService;
    private final LoggedUser loggedUser;

    public TaskController(TaskService taskService, AuthService authService, LoggedUser loggedUser) {
        this.taskService = taskService;
        this.authService = authService;
        this.loggedUser = loggedUser;
    }
    @ModelAttribute("createTaskDto")
    public CreateTaskDto initTaskDto(){
        return new CreateTaskDto();
    }

    @GetMapping("/task/add")
    public String addTask() {
        return "task-add";
    }

    @PostMapping("/task/add")
    public String addOffer(@Valid CreateTaskDto createTaskDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createTaskDto", createTaskDto);
            redirectAttributes.addFlashAttribute
                    ("org.springframework.validation.BindingResult.createTaskDto", bindingResult);

            return "redirect:/task/add";
        }

        this.taskService.saveTask(createTaskDto);

        return "redirect:/home";
    }
    @GetMapping("/task/remove/{id}")
    public String removeOffer(@PathVariable Long id){

        taskService.removeTaskById(id);

        return "redirect:/home";
    }

    @GetMapping("/task/return/{taskId}")

    public String returnTask(@PathVariable Long taskId){
        long loggedUserId = this.loggedUser.getId();

        taskService.returnTaskById(taskId,loggedUserId);

        return "redirect:/home";
    }
    @GetMapping("/task/assign/{taskId}")
    public String assignTask(@PathVariable Long taskId){
        long loggedUserId = this.loggedUser.getId();

        this.taskService.assignTaskById(taskId,loggedUserId);

        return "redirect:/home";
    }
}
