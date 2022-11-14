package com.mohit4.dodo.persistence.models;

import com.mohit4.dodo.persistence.contants.TaskConstants;

import javax.persistence.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private String status;

    private OffsetDateTime creationDate;
    private OffsetDateTime startDate;
    private OffsetDateTime lastModifiedDate;
    private OffsetDateTime closeDate;

    public Task() {}

    public Task(long id, String title, String status, OffsetDateTime creationDate, OffsetDateTime startDate, OffsetDateTime lastModifiedDate, OffsetDateTime closeDate) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.lastModifiedDate = lastModifiedDate;
        this.closeDate = closeDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public OffsetDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(OffsetDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public void createTask() {
        this.setStatus(TaskConstants.TASK_STATUS_PLANNED);
        this.setCreationDate(OffsetDateTime.now());
        this.setLastModifiedDate(OffsetDateTime.now());
    }

    public void startTask() {
        this.setStatus(TaskConstants.TASK_STATUS_INPROGRESS);
        this.setStartDate(OffsetDateTime.now());
        this.setLastModifiedDate(OffsetDateTime.now());
    }

    public void completeTask() {
        this.setStatus(TaskConstants.TASK_STATUS_COMPLETED);
        this.setCloseDate(OffsetDateTime.now());
        this.setLastModifiedDate(OffsetDateTime.now());
    }

    public String getTotalTimeOfCompletion() {
        if ( this.getCloseDate() != null ) {
            Duration duration = Duration.ofSeconds(
                    this.getStartDate().until(this.getCloseDate(), ChronoUnit.SECONDS));
            return String.format("%d hours, %d minutes and %d seconds",
                    duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        } else {
            return "Task is not finished.";
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", startDate=" + startDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", closeDate=" + closeDate +
                '}';
    }
}
