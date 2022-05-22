package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@NodeEntity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
public class GraphIssue {
    @Id
    @GeneratedValue
    private Long id;
    private String key;
    private String description;
    @Relationship(type = "IN_PROJECT", direction = "INCOMING")
    private GraphProject project;
    private Person assignee;
    private Person reporter;

    public GraphIssue(String key, String description, GraphProject project) {
        this.key = key;
        this.description = description;
        this.project = project;
    }

    @Override
    public String toString() {
        return "GraphIssue{" +
                "key='" + key + '\'' +
                '}';
    }
}
