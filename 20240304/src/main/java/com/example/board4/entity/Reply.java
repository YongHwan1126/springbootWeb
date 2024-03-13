package com.example.board4.entity;

import javax.persistence.*;

import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "REPLY")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NO")
    private Long no;

    @Column(name = "BOARD_NO")
    private Long boardNo;

    @Column(name = "WRITER_NO")
    private Long writerNo;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    
    
}