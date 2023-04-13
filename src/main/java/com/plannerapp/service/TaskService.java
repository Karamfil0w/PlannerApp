package com.plannerapp.service;

import com.plannerapp.config.LoggedUser;
import com.plannerapp.model.dtos.CreateTaskDto;
import com.plannerapp.model.entity.Priority;
import com.plannerapp.model.entity.Task;
import com.plannerapp.model.entity.User;
import com.plannerapp.repo.PriorityRepository;
import com.plannerapp.repo.TaskRepository;
import com.plannerapp.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final LoggedUser loggedUser;

    private final PriorityRepository priorityRepository;

    public TaskService(TaskRepository taskRepository, AuthService authService, UserRepository userRepository, LoggedUser loggedUser, PriorityRepository priorityRepository) {
        this.taskRepository = taskRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
        this.priorityRepository = priorityRepository;
    }

    public void saveTask(CreateTaskDto createTaskDto) {

        Task taskToSave = new Task();
        taskToSave.setDescription(createTaskDto.getDescription());
        taskToSave.setDueDate(createTaskDto.getDueDate());
        Optional<Priority> byPriorityName = this.priorityRepository.
                findByName(createTaskDto.getPriorityName());
        taskToSave.setPriority(byPriorityName.get());
        Optional<User> currentUser = this.userRepository.findById(loggedUser.getId());
        taskToSave.setUser(currentUser.get());

        this.taskRepository.save(taskToSave);

    }

    public List<Task> findAllTaskByUserId(long loggedUserId) {
        List<Task> byId = this.taskRepository.findByUserId(loggedUserId);
        return byId;
    }

    public List<Task> findAllTaskByUserIdNot(long loggedUserId) {
        List<Task> byUserIdNot = this.taskRepository.findByUserIdNot(loggedUserId);
        List<Task> allTaskByUserIsNull = findAllTaskByUserIsNull();
        byUserIdNot.addAll(allTaskByUserIsNull);
        return byUserIdNot;
    }

    public void removeTaskById(Long id) {
        Optional<Task> byId = this.taskRepository.findById(id);
        this.taskRepository.delete(byId.get());
    }

    @Transactional
    public void returnTaskById(Long taskId, long loggedUserId) {

        Optional<User> currentLoggedUser = this.userRepository.findById(loggedUserId);
        List<Task> assignedTasks = currentLoggedUser.get().getAssignedTasks();

        Optional<Task> taskToReturn = this.taskRepository.findById(taskId);

        assignedTasks.remove(taskToReturn.get());

        taskToReturn.get().setUser(null);

        userRepository.save(currentLoggedUser.get());

    }

    public List<Task> findAllTaskByUserIsNull() {
        List<Task> byUserIsNull = this.taskRepository.findByUserIsNull();

        return byUserIsNull;

    }

    @Transactional
    public void assignTaskById(Long taskId, long loggedUserId) {
        Optional<Task> taskToAssign = taskRepository.findById(taskId);
        Optional<User> currentLoggedUser = userRepository.findById(loggedUserId);

        taskToAssign.get().setUser(currentLoggedUser.get());
        this.userRepository.save(currentLoggedUser.get());
    }
}
