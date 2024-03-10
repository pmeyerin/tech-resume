package coop.stlma.tech.resume.employment.error;

import java.util.UUID;

public class NoSuchEmploymentException extends RuntimeException {
    public NoSuchEmploymentException(UUID relationId) {
        super("No employment found for id " + relationId);
    }
}
