package coop.stlma.tech.resume.project;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    private UUID projectId;
    private String projectName;
    private String projectDescription;
    private LocalDate projectStart;
    private LocalDate projectEnd;
    @Builder.Default
    private List<TechAndSkill> techAndSkills = new ArrayList<>();
}
