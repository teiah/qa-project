package api.models.models;

import api.models.basemodel.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class ExpertiseProfile extends BaseModel {

    private Integer id;
    private List<Skill> skills = new ArrayList<>();
    private Category category;
    private double availability;

    public int getId() {
        return id;
    }

    public List<Skill> getSkills() {
        return this.skills;
    }

    public Category getCategory() {
        return this.category;
    }

    public double getAvailability() {
        return this.availability;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        String expertiseProfileBodyToString = "{\n" +
                "  \"availability\": %.1f,\n" +
                "  \"category\": {\n" +
                "    \"id\": %d,\n" +
                "    \"name\": \"%s\"\n" +
                "  },\n" +
                "  \"id\": 0,\n";
        String skills = "";
        if (this.getSkills().size() == 0) {
//            expertiseProfileBodyToString = expertiseProfileBodyToString.replaceAll(",\n%s\n}$", "");
            expertiseProfileBodyToString += "}";
            return String.format(expertiseProfileBodyToString, this.availability, this.getCategory().getId(), this.getCategory().getName());
        } else {
            for (int i = 0; i < this.getSkills().size(); i++) {
                skills += String.format("  \"skill%d\": \"%s\",\n", i + 1, this.getSkills().get(i).getSkill());
            }
        }
        expertiseProfileBodyToString += "%s\n";
        skills = skills.replaceAll(",\n$", "");
        expertiseProfileBodyToString = String.format(expertiseProfileBodyToString, availability, this.getCategory().getId(), this.getCategory().getName(),
                skills);
        expertiseProfileBodyToString += "}";
        return expertiseProfileBodyToString;
    }

}
