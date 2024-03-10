package coop.stlma.tech.resume.project.controller;

import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.error.NoSuchProjectException;
import coop.stlma.tech.resume.project.service.ProjectService;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PutMapping("/techSkill/{projectId}")
    public ResponseEntity<List<TechAndSkill>> addSkillToEmployment(@PathVariable("projectId") UUID projectId,
                                                                   @RequestBody List<String> skills) {
        try {
            List<TechAndSkill> result = projectService.addSkillsToProject(projectId, skills);
            return ResponseEntity.ok(result);
        } catch (NoSuchProjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable("projectId") UUID projectId) {
        try {
            Project result = projectService.getProject(projectId);
            return ResponseEntity.ok(result);
        } catch (NoSuchProjectException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
