package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
public class GraphProject {
    @Id
    @GeneratedValue
    private Long id;
    private String key;
    @Relationship(type = "HAVE_ISSUES")
    private List<GraphIssue> issueList;

    public GraphProject(String key) {
        this.key = key;
    }

    public void setIssueList(List<GraphIssue> issueList) {
        this.issueList = issueList;
    }
}
