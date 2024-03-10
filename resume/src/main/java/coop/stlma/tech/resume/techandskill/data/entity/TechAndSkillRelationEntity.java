package coop.stlma.tech.resume.techandskill.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity(name = "tech_and_skills_relation")
@Data
public class TechAndSkillRelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID techSkillRelationId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tech_skill_id", referencedColumnName = "techSkillId")
    private TechAndSkillEntity techSkill;
    private int relationType;
    @Column(name = "relation_id")
    private UUID relationId;
}
