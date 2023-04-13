package com.plannerapp.repo;

import com.plannerapp.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByUserId(long loggedUserId);

    List<Task> findByUserIdNot(long loggedUserId);

    @Query("SELECT t FROM Task t WHERE t.user IS NULL")
    List<Task> findByUserIsNull();
}
