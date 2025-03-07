package coop.stlma.tech.resume.project.error;

public class InvalidProjectException extends RuntimeException {
    public InvalidProjectException(String message) {
        super(message);
    }
}
