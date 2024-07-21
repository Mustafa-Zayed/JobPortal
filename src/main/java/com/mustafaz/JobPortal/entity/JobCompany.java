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
@Table(name = "job_company")
public class JobCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String logo;

    private String name;

    @OneToMany(mappedBy = "jobCompanyId")
    @ToString.Exclude
    private List<JobPostActivity> jobPostActivities;

}