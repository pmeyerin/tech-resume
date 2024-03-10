package coop.stlma.tech.resume.employment.data.entity;

import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "employment")
@Data
public class EmploymentEntity {

    public static final int EMPLOYMENT_RELATION_TYPE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employmentId;

    private String employmentName;

    private String employmentDescription;

    private LocalDate employmentStart;
    private LocalDate employmentEnd;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_relation", referencedColumnName = "employmentId")
    @Where(clause = "project_relation_type = " + EMPLOYMENT_RELATION_TYPE)
    private List<ProjectEntity> projects = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "relation_id", referencedColumnName = "employmentId")
    @Where(clause = "relation_type = " + EMPLOYMENT_RELATION_TYPE)
    private List<TechAndSkillRelationEntity> techsAndSkills = new ArrayList<>();

    private UUID worker;
}
