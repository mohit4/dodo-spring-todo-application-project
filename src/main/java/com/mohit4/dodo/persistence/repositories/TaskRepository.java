package com.mohit4.dodo.persistence.repositories;

import com.mohit4.dodo.persistence.models.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> { }
