package com.github.visola.githubnotifier.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {

  @Id 
  private long id;
  private String login;
  private String name;
  private String avatarUrl;

  public User() {
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id &&
        Objects.equals(login, user.login) &&
        Objects.equals(name, user.name) &&
        Objects.equals(avatarUrl, user.avatarUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, login, name, avatarUrl);
  }
}
