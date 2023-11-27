package api.models.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ExpertiseProfileResponse {
    private int id;
    private List<Skill> skills;
    private Category category;
    private double availability;
}