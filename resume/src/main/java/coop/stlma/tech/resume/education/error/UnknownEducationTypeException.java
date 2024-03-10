package coop.stlma.tech.resume.education.error;

public class UnknownEducationTypeException extends RuntimeException {
    public UnknownEducationTypeException(String name) {
        super("No such education type: " + name);
    }
}
