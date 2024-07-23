package com.mustafaz.JobPortal.entity;

public record RecruiterJobsDto(Long totalCandidates, Integer jobPostId, String jobTitle,
                               JobLocation jobLocationId,JobCompany jobCompanyId) {
}
