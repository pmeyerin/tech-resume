package coop.stlma.tech.resume.util;

import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {

    public static List<TechAndSkillRelationEntity> makeRelations(int relationType,UUID relationId, TechAndSkillEntity... skill) {
        return List.of(skill).stream()
                .map(s -> {
                    TechAndSkillRelationEntity entity = new TechAndSkillRelationEntity();
                    entity.setRelationId(UUID.nameUUIDFromBytes(s.getTechSkillName().getBytes()));
                    entity.setTechSkill(s);
                    entity.setRelationId(relationId);
                    entity.setRelationType(relationType);
                    return entity;
                })
                .collect(Collectors.toList());
    }

    public static List<TechAndSkillRelationEntity> makeRelations(int relationType, UUID relationId, String... relation) {
        return makeRelations(relationType, relationId, Stream.of(relation)
                .map(TestUtils::makeTechAndSkill).toArray(TechAndSkillEntity[]::new));
    }

    public static TechAndSkillEntity makeTechAndSkill(String name) {
        TechAndSkillEntity entity = new TechAndSkillEntity();
        entity.setTechSkillId(UUID.nameUUIDFromBytes(name.getBytes()));
        entity.setTechSkillName(name);
        return entity;
    }
}
