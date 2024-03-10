package coop.stlma.tech.resume.techandskill.data;

import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechSkillsRepository extends JpaRepository<TechAndSkillEntity, UUID> {

    Optional<TechAndSkillEntity> findByTechSkillName(String techSkillName);

    List<TechAndSkillEntity> findByTechSkillNameIn(List<String> skills);
}
