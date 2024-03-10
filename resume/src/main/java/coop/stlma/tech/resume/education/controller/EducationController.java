package coop.stlma.tech.resume.education.controller;

import coop.stlma.tech.resume.education.error.NoSuchEducationException;
import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.service.EducationService;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.worker.error.NoSuchWorkerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping("/{workerId}")
    public ResponseEntity<Education> addEducationToWorker(@PathVariable("workerId") UUID workerId,
                                                          @RequestBody Education education) {
        try {
            Education result = educationService.addEducation(workerId, education);
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(201).body(result);
        } catch (NoSuchWorkerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/techSkill/{educationId}")
    public ResponseEntity<List<TechAndSkill>> addSkillToEducation(@PathVariable("educationId") UUID educationId,
                                                                  @RequestBody List<String> skills) {
        try {
            List<TechAndSkill> result = educationService.addSkillsToEducation(educationId, skills);
            return ResponseEntity.ok(result);
        } catch (NoSuchEducationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/project/{educationId}")
    public ResponseEntity<Project> addProjectToEducation(@PathVariable("educationId") UUID educationId,
                                                                    @RequestBody Project project) {
        try {
            Project result = educationService.addProjectToEducation(educationId, project);
            return ResponseEntity.ok(result);
        } catch (NoSuchEducationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
