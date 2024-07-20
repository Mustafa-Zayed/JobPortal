package com.mustafaz.JobPortal.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {

    @Id
    private Integer userAccountId;

    @MapsId // to indicate that the userAccountId field is both the primary key and the foreign key.
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private Users users;

    private String city;

    private String company;

    private String country;

    private String firstName;

    private String lastName;

    @Column(length = 64)
    private String profilePhoto;

    private String state;

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null) return null;
        return "/photos/recruiter/" + userAccountId + "/" + profilePhoto;
    }
}