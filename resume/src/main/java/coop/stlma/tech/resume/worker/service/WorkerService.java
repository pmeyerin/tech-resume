package coop.stlma.tech.resume.worker.service;

import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.util.EntityDtoMappingUtils;
import coop.stlma.tech.resume.worker.Worker;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public List<Worker> getAllWorkers() {
        List<Worker> ofTheJedi = new ArrayList<>();
        workerRepository.findAll()
                .forEach((workerEntity ->
                        ofTheJedi.add(new Worker(
                                workerEntity.getWorkerId(),
                                workerEntity.getWorkerName(),
                                workerEntity.getWorkerPhone(),
                                workerEntity.getWorkerEmail(),
                                workerEntity.getEmploymentHistory() != null ? workerEntity.getEmploymentHistory().stream()
                                        .map(EntityDtoMappingUtils::buildEmployment).toList(): Collections.emptyList(),
                                workerEntity.getEducationHistory() != null ? workerEntity.getEducationHistory().stream()
                                        .map(EntityDtoMappingUtils::buildEducation).toList() : Collections.emptyList(),
                                workerEntity.getProjects() != null ? workerEntity.getProjects().stream()
                                        .map(EntityDtoMappingUtils::buildProject).toList() : Collections.emptyList(),
                                workerEntity.getTechsAndSkillsEstimates() != null ? workerEntity.getTechsAndSkillsEstimates().stream()
                                        .map(EntityDtoMappingUtils::buildWorkerSkillEstimates).toList() : Collections.emptyList()
                                ))));
        return ofTheJedi;
    }

    public Worker getWorker(UUID workerId) {
        return workerRepository.findById(workerId)
                .map(workerEntity -> {
                    log.trace("something: " + workerEntity.getEmploymentHistory().get(0).getProjects().get(0).getTechsAndSkills().size());
                    return new Worker(
                        workerEntity.getWorkerId(),
                        workerEntity.getWorkerName(),
                        workerEntity.getWorkerPhone(),
                        workerEntity.getWorkerEmail(),
                        workerEntity.getEmploymentHistory() != null ? workerEntity.getEmploymentHistory().stream()
                                .map(EntityDtoMappingUtils::buildEmployment).toList(): Collections.emptyList(),
                        workerEntity.getEducationHistory() != null ? workerEntity.getEducationHistory().stream()
                                .map(EntityDtoMappingUtils::buildEducation).toList() : Collections.emptyList(),
                        workerEntity.getProjects() != null ? workerEntity.getProjects().stream()
                                .map(EntityDtoMappingUtils::buildProject).toList() : Collections.emptyList(),
                        workerEntity.getTechsAndSkillsEstimates() != null ? workerEntity.getTechsAndSkillsEstimates().stream()
                                .map(EntityDtoMappingUtils::buildWorkerSkillEstimates).toList() : Collections.emptyList()
                );}).orElse(null);
    }

    public Project addProjectToWorker(UUID workerId, Project project) {
        WorkerEntity saveMe = workerRepository.findById(workerId)
                .orElseThrow(() -> new NoSuchEmploymentException(workerId));
        if (saveMe.getProjects() == null) {
            saveMe.setProjects(new ArrayList<>());
        }
        saveMe.getProjects().add(EntityDtoMappingUtils.buildProjectEntity(project, WorkerEntity.WORKER_PROJECT_TYPE));
        saveMe = workerRepository.save(saveMe);
        //TODO: handle the case where a project already exists with that name for this worker
        return saveMe.getProjects().stream()
                .filter(projectEntity ->
                        projectEntity.getProjectName().equals(project.getProjectName()))
                .findFirst()
                .map(EntityDtoMappingUtils::buildProject)
                .orElse(null);
    }
}
