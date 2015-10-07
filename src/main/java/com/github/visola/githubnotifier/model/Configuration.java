package com.github.visola.githubnotifier.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Configuration {

  @Id
  private String username;
  private String password;
  private String githubUrl;

  public String getGithubUrl() {
    return githubUrl;
  }

  public void setGithubUrl(String githubUrl) {
    this.githubUrl = githubUrl;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
