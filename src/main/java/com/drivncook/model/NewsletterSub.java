package com.drivncook.model;

public class NewsletterSub {
    private String id;
    private String email;
    private String subject;
    private String content;

    public NewsletterSub() {}

    public NewsletterSub(String id, String email, String subject, String content) {
        this.id = id;
        this.email = email;
        this.subject = subject;
        this.content = content;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
