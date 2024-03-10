package coop.stlma.tech.resume.project.data;

import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {


    @Query("" +
            "SELECT p " +
            "FROM project p " +
            "INNER JOIN  p.techsAndSkills t " +
            "   ON t.techSkill.techSkillId = ?2 " +
            "WHERE t.techSkill.techSkillId = ?2 " +
            "AND ((p.projectRelationType = " + WorkerEntity.WORKER_PROJECT_TYPE + " " +
            "AND t.relationId IN (SELECT p.projectId " +
            "                           FROM project p " +
            "                           WHERE p.projectRelation = ?1 " +
            "                           AND p.projectRelationType = " + WorkerEntity.WORKER_PROJECT_TYPE + "))" +
            "OR (p.projectRelationType = " + EducationEntity.EDUCATION_RELATION_TYPE + " " +
            "    AND t.relationId IN (SELECT p.projectId "  +
            "                           FROM project p " +
            "                           JOIN education edu " +
            "                            ON edu.educationId = p.projectRelation " +
            "                           WHERE edu.worker = ?1 "  +
            "                           AND p.projectRelationType = " + EducationEntity.EDUCATION_RELATION_TYPE + "))" +
            "OR (p.projectRelationType = " + EmploymentEntity.EMPLOYMENT_RELATION_TYPE + " " +
            "    AND t.relationId IN (SELECT p.projectId "  +
            "                           FROM project p " +
            "                           JOIN employment emp " +
            "                            ON emp.employmentId = p.projectRelation " +
            "                           WHERE emp.worker = ?1 "  +
            "                           AND p.projectRelationType = " + EmploymentEntity.EMPLOYMENT_RELATION_TYPE + ")))")
    List<ProjectEntity> findByWorkerAndSkill(UUID workerId, UUID skillId);
}
