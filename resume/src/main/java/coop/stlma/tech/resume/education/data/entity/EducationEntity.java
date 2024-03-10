package coop.stlma.tech.resume.education.data.entity;

import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "education")
@Data
public class EducationEntity {
    public static final int EDUCATION_RELATION_TYPE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID educationId;

    private String institutionName;
    private String program;
    private LocalDate startDate;
    private LocalDate graduationPeriod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "education_type")
    private EducationTypeEntity educationType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_relation", referencedColumnName = "educationId")
    @Where(clause = "project_relation_type = " + EDUCATION_RELATION_TYPE)
    private List<ProjectEntity> projects = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "relation_id", referencedColumnName = "educationId")
    @Where(clause = "relation_type = " + EDUCATION_RELATION_TYPE)
    private List<TechAndSkillRelationEntity> techsAndSkills = new ArrayList<>();

    private UUID worker;

}
