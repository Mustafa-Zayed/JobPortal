package com.mustafaz.JobPortal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_location")
public class JobLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String city;

    private String country;

    private String state;

    @OneToMany(mappedBy = "jobLocationId")
    @ToString.Exclude
    private List<JobPostActivity> jobPostActivities;

}