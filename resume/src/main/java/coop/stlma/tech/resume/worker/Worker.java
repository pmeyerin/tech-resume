package coop.stlma.tech.resume.worker;

import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker {
    private UUID workerId;

    private String workerName;

    private String workerPhone;

    private String workerEmail;
    private List<Employment> employmentHistory = new ArrayList<>();
    private List<Education> educationHistory = new ArrayList<>();
    private List<Project> hobbyProjects = new ArrayList<>();
    private List<WorkerSkillEstimate> workerSkillEstimates = new ArrayList<>();
}
