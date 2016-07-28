package com.github.visola.githubnotifier.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.common.base.Strings;

@Entity
public class Configuration {

  @Id
  private String username;
  private String password;
  private boolean token = false;
  private String githubUrl;

  @Transient
  public boolean isValid() {
    return !Strings.isNullOrEmpty(githubUrl)
        && !Strings.isNullOrEmpty(username)
        && !Strings.isNullOrEmpty(password);
  }

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

  public boolean isToken() {
    return token;
  }

  public void setToken(boolean token) {
    this.token = token;
  }

}
