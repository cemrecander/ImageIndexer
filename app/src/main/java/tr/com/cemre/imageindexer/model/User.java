package tr.com.cemre.imageindexer.model;

public class User {
    String name, email, userRef;

    public User() {

    }

    public User(String name, String email, String userRef) {
        this.name = name;
        this.email = email;
        this.userRef = userRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRef() {
        return userRef;
    }
}
