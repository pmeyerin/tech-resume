package coop.stlma.tech.resume.project.error;

import java.util.UUID;

public class NoSuchProjectException extends RuntimeException {
    public NoSuchProjectException(UUID relationId) {
        super("No project found for id " + relationId);
    }
}
