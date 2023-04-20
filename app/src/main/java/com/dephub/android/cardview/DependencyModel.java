package com.dephub.android.cardview;

public class DependencyModel {

    private String dependencyName;

    private String developerName;

    private String githubLink;

    private final String cardBackground;

    private String youtubeLink;

    private String id;

    private String license;

    private String licenseLink;

    private String fullName;

    public DependencyModel(String dependencyName, String developerName, String githubLink, String cardBackground, String youtubeLink, String id, String license, String licenseLink, String fullName) {
        this.dependencyName = dependencyName;
        this.developerName = developerName;
        this.githubLink = githubLink;
        this.cardBackground = cardBackground;
        this.youtubeLink = youtubeLink;
        this.id = id;
        this.license = license;
        this.licenseLink = licenseLink;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getGithubLink() {
        return githubLink;
    }

    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseLink() {
        return licenseLink;
    }

    public void setLicenseLink(String licenseLink) {
        this.licenseLink = licenseLink;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}