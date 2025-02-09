package org.example.dto.addmodule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddModuleDTO {
    @JsonProperty("name")
    String name;
    @JsonProperty("questions")
    List<String> questions;

    public AddModuleDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddModuleDTO that = (AddModuleDTO) o;
        return name.equals(that.name) && questions.equals(that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, questions);
    }

    @Override
    public String toString() {
        return "AddModuleDTO{" +
                "name='" + name + '\'' +
                ", questions=" + questions +
                '}';
    }
}
