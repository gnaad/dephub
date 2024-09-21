package com.dephub.android.cardview;

public class Dependency {

    private String id;
    private String dependencyName;
    private String developerName;
    private String fullName;
    private String githubLink;

    public Dependency(String id, String dependencyName, String developerName, String fullName, String githubLink) {
        this.id = id;
        this.dependencyName = dependencyName;
        this.developerName = developerName;
        this.fullName = fullName;
        this.githubLink = githubLink;
    }

    public Dependency() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGithubLink() {
        return githubLink;
    }
}