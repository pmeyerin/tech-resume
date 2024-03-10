package coop.stlma.tech.resume.worker.controller;

import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.worker.Worker;
import coop.stlma.tech.resume.worker.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/worker")
@Slf4j
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        log.trace("in controller");
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @GetMapping("/{workerId}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Worker> workerById(@PathVariable("workerId") UUID workerId) {
        log.trace("in controller");
        return ResponseEntity.ok(workerService.getWorker(workerId));
    }

    @PutMapping("/project/{workerId}")
    public ResponseEntity<Project> addProjectToEmployment(@PathVariable("workerId") UUID workerId,
                                                          @RequestBody Project project) {
        try {
            Project result = workerService.addProjectToWorker(workerId, project);
            return ResponseEntity.ok(result);
        } catch (NoSuchEmploymentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
