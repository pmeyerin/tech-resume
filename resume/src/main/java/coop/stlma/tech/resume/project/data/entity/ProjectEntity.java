package coop.stlma.tech.resume.project.data.entity;

import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

@Entity(name = "project")
@Data
public class ProjectEntity {

    public static final int PROJECT_RELATION_TYPE = 3;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "project_id")
    private UUID projectId;
    private String projectName;
    private String projectDescription;
    private LocalDate projectStart;
    private LocalDate projectEnd;
    @Column(name = "project_relation")
    private UUID projectRelation;
    private int projectRelationType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "relation_id", referencedColumnName = "project_id")
    @Where(clause = "relation_type = " + PROJECT_RELATION_TYPE)
    private List<TechAndSkillRelationEntity> techsAndSkills = new ArrayList<>();
}
