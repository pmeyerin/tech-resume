package coop.stlma.tech.resume.techandskill.controller;

import coop.stlma.tech.resume.techandskill.SkillGrouping;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.service.TechSkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tech-skills")
public class TechSkillController {

    private final TechSkillsService techSkillsService;

    public TechSkillController(TechSkillsService techSkillsService) {
        this.techSkillsService = techSkillsService;
    }

    @GetMapping
    public ResponseEntity<List<TechAndSkill>> getAllTechSkills() {
        return ResponseEntity.ok(techSkillsService.getAllTechSkills());
    }

    @GetMapping("/{name}")
    public ResponseEntity<TechAndSkill> getByName(@PathVariable("name") String name) {
        TechAndSkill result = techSkillsService.getByName(name);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> bulkSave(@RequestBody List<String> techSkill) {
        techSkillsService.bulkSave(techSkill);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/related/{techSkillId}/{workerId}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<SkillGrouping> getRelated(@PathVariable("techSkillId") UUID techSkillId,
                                                    @PathVariable("workerId") UUID workerId) {
        SkillGrouping result = techSkillsService.getByWorkerAndSkill(workerId, techSkillId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
