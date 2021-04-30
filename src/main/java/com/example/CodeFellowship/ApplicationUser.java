package com.example.CodeFellowship;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class ApplicationUser implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String bio;


    @OneToMany(mappedBy = "applicationUser")
    List<PostModel> posts;

    @ManyToMany
    @JoinTable(
            name = "follow_users",
            joinColumns = {@JoinColumn(name="usersIFollow")},
            inverseJoinColumns = {@JoinColumn(name="usersFollowingMe")}
    )

    Set<ApplicationUser> usersIFollow;

    @ManyToMany(mappedBy = "usersIFollow")
    List<ApplicationUser> usersFollowingMe;


    public ApplicationUser(){

    }

    public ApplicationUser( String userName, String password, String firstName, String lastName, String dateOfBirth, String bio) {
        this.username = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<PostModel> getPosts() {
        return this.posts;
    }

    public Set<ApplicationUser> getUsersIFollow() {
        return usersIFollow;
    }

    public List<ApplicationUser> getUsersFollowingMe() {
        return usersFollowingMe;
    }

    public void addUserToFollow(ApplicationUser userToFollow){
        usersIFollow.add(userToFollow);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
