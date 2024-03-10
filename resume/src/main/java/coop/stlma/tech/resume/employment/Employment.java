package coop.stlma.tech.resume.employment;

import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Employment {
    private UUID employmentId;

    private String employmentName;

    private String employmentDescription;

    private LocalDate employmentStart;
    private LocalDate employmentEnd;
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
    @Builder.Default
    private List<TechAndSkill> techAndSkills = new ArrayList<>();
}
