package com.github.visola.githubnotifier.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.common.base.Strings;

@Entity
public class Configuration {

  private static final String GH_PUBLIC_API = "https://api.github.com";
  private static final String GH_ENTERPRISE_BASE_PATH = "/api/v3";

  @Id
  private String username;
  private String password;
  private boolean token = false;
  private String githubUrl;

  @Transient
  public String getApiBase() {
    if (!Strings.isNullOrEmpty(githubUrl)) {
      if (githubUrl.startsWith(GH_PUBLIC_API)) {
        return GH_PUBLIC_API + "/";
      } else {
        return githubUrl + GH_ENTERPRISE_BASE_PATH;
      }
    }
    return null;
  }

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
