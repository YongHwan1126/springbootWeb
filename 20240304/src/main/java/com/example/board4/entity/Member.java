package com.example.board4.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "MEMBER")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NO")
    private Long no;

    @Column(name = "ID", unique = true, nullable = false)
    private String id;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    private String role;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "HOBBY")
    private String hobby;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "ENROLL_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date enrollDate;

    @Column(name = "MODIFY_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

}