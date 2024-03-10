package coop.stlma.tech.resume.education;

import coop.stlma.tech.resume.education.data.EducationType;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    private UUID educationId;
    private String institutionName;
    private String program;
    private LocalDate startDate;
    private LocalDate graduationPeriod;
    private EducationType educationType;
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
    @Builder.Default
    private List<TechAndSkill> techAndSkills = new ArrayList<>();
}
