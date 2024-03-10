package coop.stlma.tech.resume.education.error;

import java.util.UUID;

public class NoSuchEducationException extends RuntimeException {
    public NoSuchEducationException(UUID educationId) {
        super("No education found for id " + educationId);
    }
}
