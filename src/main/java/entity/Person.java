package entity;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@NodeEntity
public class Person {
    String name;
    List<GraphIssue> issueList;
}
