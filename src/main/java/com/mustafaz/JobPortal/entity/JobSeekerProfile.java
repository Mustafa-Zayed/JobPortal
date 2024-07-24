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
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private Users users;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Skills> skills;

    private String city;

    private String country;

    private String employmentType;

    private String firstName;

    private String lastName;

    @Column(length = 64)
    private String profilePhoto;

    private String resume;

    private String state;

    private String workAuthorization;

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null || userAccountId == null) return null;
        return "/photos/candidate/" + userAccountId + "/" + profilePhoto;
    }
}