package coop.stlma.tech.resume.techandskill;

import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.project.Project;
import lombok.Data;

import java.util.List;

@Data
public class SkillGrouping {
    List<Project> projects;
    List<Employment> employments;
    List<Education> educations;
}
