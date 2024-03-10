package coop.stlma.tech.resume.worker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerSkillEstimate {
    private UUID techSkillId;
    private String techSkillName;
    private int yearsSkillsEstimate;
}
