package com.plannerapp.init;

import com.plannerapp.model.entity.Priority;
import com.plannerapp.model.enums.PriorityName;
import com.plannerapp.repo.PriorityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrioritySeeder implements CommandLineRunner {
    private final PriorityRepository priorityRepository;

    public PrioritySeeder(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.priorityRepository.count() == 0) {
            List<Priority> priorities = Arrays.stream(PriorityName.values())
                    .map(Priority::new)
                    .collect(Collectors.toList());
            this.priorityRepository.saveAll(priorities);
        }
    }
}
