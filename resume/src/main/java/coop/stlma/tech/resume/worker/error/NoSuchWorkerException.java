package coop.stlma.tech.resume.worker.error;

import java.util.UUID;

public class NoSuchWorkerException extends RuntimeException {
    public NoSuchWorkerException(UUID workerId) {
        super("No worker found for id " + workerId);
    }
}
