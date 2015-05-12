package entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Greeting")
public class Greeting {
  private Long id;

  private String author;
  private Date date;
  private String content;

  public Greeting() {
  }

  public Greeting(String author, Date date, String content) {
    this.author = author;
    this.date = date;
    this.content = content;
  }

  @Id
  @GeneratedValue(generator="incrementGreeting")
  @GenericGenerator(name="incrementGreeting", strategy = "increment")
  public Long getId() {
    return id;
  }

  private void setId(Long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
