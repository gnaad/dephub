package com.dephub.android.cardview;

public class Cardmodel {

    private String dependencyname;

    private String developername;

    private String githublink;

    private final String cardbackground;

    private String youtubelink;

    private String id;

    private String license;

    private String licenselink;

    private String fullname;

    public Cardmodel(String dependencyname,String developername,String githublink,String cardbackground,String youtubelink,String id,String license,String licenselink,String fullname) {
        this.dependencyname = dependencyname;
        this.developername = developername;
        this.githublink = githublink;
        this.cardbackground = cardbackground;
        this.youtubelink = youtubelink;
        this.id = id;
        this.license = license;
        this.licenselink = licenselink;
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
    }

    public String getDependencyname() {
        return dependencyname;
    }

    public void setDependencyname(String dependencyname) {
        this.dependencyname = dependencyname;
    }

    public String getDevelopername() {
        return developername;
    }

    public void setDevelopername(String developername) {
        this.developername = developername;
    }

    public String getGithublink() {
        return githublink;
    }

    public void setGithublink(String githublink) {
        this.githublink = githublink;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenselink() {
        return licenselink;
    }

    public void setLicenselink(String licenselink) {
        this.licenselink = licenselink;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}