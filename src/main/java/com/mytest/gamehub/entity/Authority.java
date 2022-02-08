package com.mytest.gamehub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity (name="authorities")
public class Authority {

    @Id
    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Authority is mandatory")
    private String authority;

    public Authority() {
    }

    public Authority(String email, String authority) {
        this.email = email;
        this.authority = authority;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
