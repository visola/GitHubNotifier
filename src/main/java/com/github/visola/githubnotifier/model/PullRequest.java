package com.github.visola.githubnotifier.model;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Table(name="pull_requests")
public class PullRequest {

  @Id
  long id;
  int number;
  String title;
  String state;
  String htmlUrl;
  Calendar createdAt;
  Calendar updatedAt;
  @JoinColumn(name="base_sha") @ManyToOne(cascade=CascadeType.MERGE) Commit base;
  @JoinColumn(name="head_sha") @ManyToOne(cascade=CascadeType.MERGE) Commit head;

  public PullRequest() {
  }

  public PullRequest(org.eclipse.egit.github.core.PullRequest pr) {
    id = pr.getId();
    number = pr.getNumber();
    title = pr.getTitle();
    state = pr.getState();
    htmlUrl = pr.getHtmlUrl();

    createdAt = Calendar.getInstance();
    createdAt.setTime(pr.getCreatedAt());

    updatedAt = Calendar.getInstance();
    updatedAt.setTime(pr.getUpdatedAt());

    base = new Commit(pr.getBase());
    head = new Commit(pr.getHead());
  }

  public Commit getBase() {
    return base;
  }

  public Calendar getCreatedAt() {
    return createdAt;
  }

  public Commit getHead() {
    return head;
  }

  public String getHtmlUrl() {
    return htmlUrl;
  }

  public long getId() {
    return id;
  }

  public int getNumber() {
    return number;
  }

  public String getState() {
    return state;
  }

  public String getTitle() {
    return title;
  }

  public Calendar getUpdatedAt() {
    return updatedAt;
  }

  public void setBase(Commit base) {
    this.base = base;
  }

  public void setCreatedAt(Calendar createdAt) {
    this.createdAt = createdAt;
  }

  public void setHead(Commit head) {
    this.head = head;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUpdatedAt(Calendar updatedAt) {
    this.updatedAt = updatedAt;
  }

}
