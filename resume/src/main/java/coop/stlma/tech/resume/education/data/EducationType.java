package coop.stlma.tech.resume.education.data;

import coop.stlma.tech.resume.education.error.UnknownEducationTypeException;

import java.util.Arrays;

public enum EducationType {
    CERTIFICATION("certification"), UNDERGRADUATE("undergraduate"), GRADUATE("graduate");

    private final String name;
    EducationType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EducationType ofName(String name) {
        return Arrays.stream(EducationType.values())
                .filter(educationType -> educationType.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new UnknownEducationTypeException(name));
    }
}
