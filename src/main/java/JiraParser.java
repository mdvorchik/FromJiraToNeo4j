import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import entity.GraphIssue;
import entity.GraphProject;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JiraParser {
    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        JiraRestClient jiraRestClient = new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(new URI("http://localhost:8080/"), "Max", "password1234");

        ProjectRestClient projectClient = jiraRestClient.getProjectClient();
        IssueRestClient issueRestClient = jiraRestClient.getIssueClient();
        String lastProjectName = null;
        String lastProjectKey = null;
        for (BasicProject project : projectClient.getAllProjects().get()) {
            Project realProject = projectClient.getProject(project.getKey()).get();
            System.out.println(project.getName());
            System.out.println(project.getKey());
            lastProjectName = project.getName();
            lastProjectKey = project.getKey();
        }
        Promise<SearchResult> searchJqlPromise = jiraRestClient.getSearchClient().searchJql("project = " + lastProjectName + " ORDER BY assignee, resolutiondate");

        //create objects for neo4j
        GraphProject graphProject = new GraphProject(lastProjectKey);
        List<GraphIssue> graphIssues = new ArrayList<GraphIssue>();

        for (Issue issue : searchJqlPromise.claim().getIssues()) {
            System.out.println(issue.getKey());
            graphIssues.add(new GraphIssue(issue.getKey(), issue.getDescription(), graphProject));
        }

        graphProject.setIssueList(graphIssues);
        //created objects for neo4j

        Configuration conf = new Configuration.Builder().build();
        SessionFactory factory = new SessionFactory(conf, "entity");

        //neo4j session creation
        Session session = factory.openSession();

        session.save(graphProject);

        List<GraphIssue> graphIssuesFromDB = (List<GraphIssue>) session.loadAll(GraphIssue.class);
        for (GraphIssue graphIssue : graphIssuesFromDB) {
            System.out.println(graphIssue);
        }

    }
}
