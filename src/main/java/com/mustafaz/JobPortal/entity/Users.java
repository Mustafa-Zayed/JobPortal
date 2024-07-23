package com.mustafaz.JobPortal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @ManyToOne(cascade = CascadeType.ALL) // we should not cascade on delete, but in general we won't delete users in the application.
    @JoinColumn(name = "user_type_id")
    private UsersType userTypeId;

/*
    @ToString.Exclude
    @OneToMany(mappedBy = "postedById", cascade = CascadeType.ALL)
    private List<JobPostActivity> jobPostActivities;
*/

    @Column(unique = true)
    private String email;

    @NotEmpty
    private String password;

    private Boolean isActive;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date registrationDate;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(
                new SimpleGrantedAuthority(userTypeId.getUserTypeName())
        );
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }
}