package coop.stlma.tech.resume.techandskill.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity(name = "tech_and_skills")
@Data
public class TechAndSkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID techSkillId;

    private String techSkillName;
}
