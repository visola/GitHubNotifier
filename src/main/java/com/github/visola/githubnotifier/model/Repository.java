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
public class Repository {

  @Id
  private long id;
  private String fullName;
  private String name;
  private String htmlUrl;

  public Repository() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getHtmlUrl() {
    return htmlUrl;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Repository that = (Repository) o;
    return id == that.id &&
        Objects.equals(fullName, that.fullName) &&
        Objects.equals(name, that.name) &&
        Objects.equals(htmlUrl, that.htmlUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, name, htmlUrl);
  }
}
