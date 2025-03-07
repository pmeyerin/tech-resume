package coop.stlma.tech.resume.employment.controller;

import coop.stlma.tech.resume.employment.error.InvalidEmploymentException;
import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.service.EmploymentService;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.worker.error.NoSuchWorkerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employment")
public class EmploymentController {
    private final EmploymentService employmentService;

    @Value("${coop.stlma.tech.resume.update-token}")
    private String updateToken;

    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @PostMapping("/{workerId}")
    public ResponseEntity<Employment> addEmploymentToWorker(@PathVariable("workerId") UUID workerId,
                                                            @RequestBody Employment employment) {
        try {
            Employment result = employmentService.addEmployment(workerId, employment);
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(201).body(result);
        } catch (NoSuchWorkerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Employment> updateEmployment(@RequestBody Employment employment) {
        try {
            Employment result = employmentService.updateEmployment(employment);
            return ResponseEntity.ok().body(result);
        } catch (NoSuchEmploymentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/techSkill/{employmentId}")
    public ResponseEntity<List<TechAndSkill>> addSkillToEmployment(@PathVariable("employmentId") UUID employmentId,
                                                                    @RequestBody List<String> skills) {
        try {
            List<TechAndSkill> result = employmentService.addSkillsToEmployment(employmentId, skills);
            return ResponseEntity.ok(result);
        } catch (NoSuchEmploymentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/project/{employmentId}")
    public ResponseEntity<Project> addProjectToEmployment(@PathVariable("employmentId") UUID employmentId,
                                                         @RequestBody Project project) {
        try {
            Project result = employmentService.addProjectToEmployment(employmentId, project);
            return ResponseEntity.ok(result);
        } catch (NoSuchEmploymentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(NoSuchEmploymentException.class)
    public ResponseEntity<Void> handleNoSuchEmploymentException(NoSuchEmploymentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidEmploymentException.class)
    public ResponseEntity<String> handleInvalidEmploymentException(InvalidEmploymentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
