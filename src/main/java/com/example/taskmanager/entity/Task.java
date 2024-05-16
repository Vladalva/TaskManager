package com.example.taskmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Setter
@Getter
@Entity
public class Task {
    @Id
    private UUID id;

    @Column(length = 1000)
    private String description;

    private Boolean isCompleted;
}
