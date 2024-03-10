package coop.stlma.tech.resume.worker.service;

import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.data.entity.WorkerSkillEstimateRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerTechSkillEstimateEntity;
import coop.stlma.tech.resume.worker.error.NoSuchWorkerException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional
public class WorkerSkillUpdateService {

    private final WorkerRepository workerRepository;
    private final WorkerSkillEstimateRepository workerSkillEstimateRepository;

    public WorkerSkillUpdateService(WorkerRepository workerRepository,
                                    WorkerSkillEstimateRepository workerSkillEstimateRepository) {
        this.workerRepository = workerRepository;
        this.workerSkillEstimateRepository = workerSkillEstimateRepository;
    }

    private Set<TechAndSkillEntity> allSkillsForWorker(WorkerEntity worker) {
        Set<TechAndSkillEntity> flattened = new HashSet<>();
        worker.getEmploymentHistory().forEach(employment -> {
            flattened.addAll(employment.getTechsAndSkills().stream()
                    .map(TechAndSkillRelationEntity::getTechSkill)
                    .toList());
            flattened.addAll(employment.getProjects().stream()
                    .map(ProjectEntity::getTechsAndSkills)
                    .flatMap(techAndSkillRelationEntities -> techAndSkillRelationEntities.stream()
                        .map(TechAndSkillRelationEntity::getTechSkill))
                        .toList());
        });

        log.trace("employment skills: {}", flattened.stream()
                .map(TechAndSkillEntity::getTechSkillName).collect(Collectors.joining(", ")));

        worker.getEducationHistory().forEach(education -> {
            flattened.addAll(education.getTechsAndSkills().stream()
                    .map(TechAndSkillRelationEntity::getTechSkill)
                    .toList());
            flattened.addAll(education.getProjects().stream()
                    .map(ProjectEntity::getTechsAndSkills)
                    .flatMap(techAndSkillRelationEntities -> techAndSkillRelationEntities.stream()
                            .map(TechAndSkillRelationEntity::getTechSkill))
                    .toList());
        });

        log.trace("education skills: {}", flattened.stream()
                .map(TechAndSkillEntity::getTechSkillName).collect(Collectors.joining(", ")));

        worker.getProjects().forEach(project -> flattened.addAll(project.getTechsAndSkills().stream()
                .map(TechAndSkillRelationEntity::getTechSkill)
                .toList()));

        log.trace("project skills: {}", flattened.stream()
                .map(TechAndSkillEntity::getTechSkillName).collect(Collectors.joining(", ")));

        return flattened;
    }

    public void updateWorkerSkillHistory(UUID workerId) {
        WorkerEntity workerEntity = workerRepository.findById(workerId)
                .orElseThrow(() -> new NoSuchWorkerException(workerId));

        Set<TechAndSkillEntity> allSkills = allSkillsForWorker(workerEntity);
        workerSkillEstimateRepository.deleteByWorker(workerEntity.getWorkerId());
        workerSkillEstimateRepository.flush();
//        workerSkillEstimateRepository.deleteAll(workerEntity.getTechsAndSkillsEstimates());

        allSkills.forEach(skill -> updateSkillEstimates(workerEntity, skill));
    }

    private void updateSkillEstimates(WorkerEntity workerEntity, TechAndSkillEntity skill) {
        log.trace("Updating estimates for {}", skill.getTechSkillName());
        List<SkillDateRange> ranges = findAllDateRanges(skill, workerEntity)
                .stream()
                .sorted(Comparator.comparing(SkillDateRange::getStart))
                .toList();

        log.trace("Ranges: {}", ranges.stream()
                .map(skillDateRange -> skillDateRange.start + "-" + skillDateRange.end)
                .collect(Collectors.joining(", ")));

        List<SkillDateRange> nonOverlappingRanges = new ArrayList<>();

        ranges.forEach(skillDateRange -> {
            //Ensure we are dealing with a date that is not already in the non-overlapping list
            if (skillDateRange.getStart().isAfter(nonOverlappingRanges.stream()
                    .max(Comparator.comparing(SkillDateRange::getEnd))
                    .map(SkillDateRange::getEnd).orElse(LocalDate.EPOCH))) {
                LocalDate end = ranges.stream()
                        .filter(otherRange -> otherRange.getStart().isBefore(Optional.ofNullable(skillDateRange.getEnd()).orElse(LocalDate.now())))
                        .max(Comparator.comparing(sdr -> Optional.ofNullable(sdr.end).orElse(LocalDate.now())))
                        .map(SkillDateRange::getEnd)
                        .orElse(skillDateRange.getEnd());

                nonOverlappingRanges.add(new SkillDateRange(skillDateRange.getStart(), end));
            }
        });

        log.trace("Non overlapping ranges: {}", nonOverlappingRanges.stream()
                .map(skillDateRange -> skillDateRange.start + "-" + skillDateRange.end)
                .collect(Collectors.joining(", ")));

        long skillDays = nonOverlappingRanges.stream()
                .map(skillDateRange ->
                        ChronoUnit.DAYS.between(skillDateRange.getStart(), Optional.ofNullable(skillDateRange.getEnd()).orElse(LocalDate.now())))
                .reduce(Long::sum).orElse(0L);
        Optional<WorkerTechSkillEstimateEntity> estimate = workerEntity.getTechsAndSkillsEstimates().stream()
                .filter(workerTechSkillEstimateEntity -> workerTechSkillEstimateEntity.getTechSkill().getTechSkillId().equals(skill.getTechSkillId()))
                .findFirst();
        if (estimate.isEmpty()) {
            WorkerTechSkillEstimateEntity newEstimate = new WorkerTechSkillEstimateEntity();
            newEstimate.setWorker(workerEntity.getWorkerId());
            newEstimate.setTechSkill(skill);
            estimate = Optional.of(newEstimate);
            workerEntity.getTechsAndSkillsEstimates().add(newEstimate);
        }
        estimate.get().setSkillDaysEstimate((int) skillDays);
        workerRepository.save(workerEntity);
    }

    private List<SkillDateRange> findAllDateRanges(TechAndSkillEntity entity, WorkerEntity workerEntity) {
        List<SkillDateRange> ranges = new ArrayList<>();

        workerEntity.getEducationHistory()
                .forEach(educationEntity -> {
                    if (educationEntity.getTechsAndSkills().stream()
                            .anyMatch(techAndSkillRelationEntity ->
                                    techAndSkillRelationEntity.getTechSkill().getTechSkillId()
                                            .equals(entity.getTechSkillId()))) {
                        SkillDateRange thisRange = new SkillDateRange(educationEntity.getStartDate(),
                                Optional.ofNullable(educationEntity.getGraduationPeriod()).orElse(LocalDate.now()));
                        extendRanges(educationEntity.getProjects().stream()
                                .filter(projectEntity -> projectEntity.getTechsAndSkills().stream()
                                        .anyMatch(techAndSkillRelationEntity -> techAndSkillRelationEntity.getTechSkill()
                                                .getTechSkillId().equals(entity.getTechSkillId()))).toList(), thisRange);
                        ranges.add(thisRange);
                    }
                    else {
                        educationEntity.getProjects().stream()
                                .filter(projectEntity -> projectEntity.getTechsAndSkills().stream()
                                        .anyMatch(techAndSkillRelationEntity ->
                                                techAndSkillRelationEntity.getTechSkill().getTechSkillId()
                                                        .equals(entity.getTechSkillId())))
                                .forEach(projectEntity -> ranges.add(new SkillDateRange(projectEntity.getProjectStart(), projectEntity.getProjectEnd())));
                    }
                });

        workerEntity.getEmploymentHistory()
                .forEach(employmentEntity -> {
                    if (employmentEntity.getTechsAndSkills().stream()
                            .anyMatch(techAndSkillRelationEntity ->
                                    techAndSkillRelationEntity.getTechSkill().getTechSkillId()
                                            .equals(entity.getTechSkillId()))) {
                        SkillDateRange thisRange = new SkillDateRange(employmentEntity.getEmploymentStart(),
                                Optional.ofNullable(employmentEntity.getEmploymentEnd()).orElse(LocalDate.now()));
                        extendRanges(employmentEntity.getProjects().stream()
                                .filter(projectEntity -> projectEntity.getTechsAndSkills().stream()
                                        .anyMatch(techAndSkillRelationEntity -> techAndSkillRelationEntity.getTechSkill()
                                                .getTechSkillId().equals(entity.getTechSkillId()))).toList(), thisRange);
                        ranges.add(thisRange);
                    } else {
                        employmentEntity.getProjects().stream()
                                .filter(projectEntity -> projectEntity.getTechsAndSkills().stream()
                                        .anyMatch(techAndSkillRelationEntity ->
                                                techAndSkillRelationEntity.getTechSkill().getTechSkillId()
                                                        .equals(entity.getTechSkillId())))
                                .forEach(projectEntity -> ranges.add(new SkillDateRange(projectEntity.getProjectStart(), projectEntity.getProjectEnd())));
                    }
                });

        workerEntity.getProjects()
                .forEach(projectEntity -> {
                    if (projectEntity.getTechsAndSkills().stream()
                            .anyMatch(techAndSkillRelationEntity ->
                                    techAndSkillRelationEntity.getTechSkill().getTechSkillId()
                                            .equals(entity.getTechSkillId()))) {
                        ranges.add(new SkillDateRange(projectEntity.getProjectStart(), Optional.ofNullable(projectEntity.getProjectEnd()).orElse(LocalDate.now())));
                    }
                });
        return ranges;
    }

    private SkillDateRange extendRanges(List<ProjectEntity> projectEntities, SkillDateRange thisRange) {
        projectEntities.stream()
                .filter(projectEntity -> projectEntity.getProjectStart().isBefore(thisRange.getStart()))
                .min(Comparator.comparing(ProjectEntity::getProjectStart))
                .ifPresent(projectEntity -> thisRange.setStart(projectEntity.getProjectStart()));
        projectEntities.stream()
                .filter(projectEntity -> Optional.ofNullable(projectEntity.getProjectEnd()).orElse(LocalDate.now()).isAfter(thisRange.getEnd()))
                .max(Comparator.comparing(ProjectEntity::getProjectEnd))
                .ifPresent(projectEntity -> thisRange.setEnd(Optional.ofNullable(projectEntity.getProjectEnd()).orElse(LocalDate.now())));
        return thisRange;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class SkillDateRange {
        private LocalDate start;
        private LocalDate end;
    }
}
