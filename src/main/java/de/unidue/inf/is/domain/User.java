package de.unidue.inf.is.domain;

public final class User {

    private String firstname;
    private String lastname;
    private String email;
    private String explanation;

    public User() {
    }
    public User(String firstname, String lastname,
                String email, String explanation)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.explanation = explanation;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail(){
        return email;
    }
    public String getExplanation()
    {
        return explanation;
    }
}