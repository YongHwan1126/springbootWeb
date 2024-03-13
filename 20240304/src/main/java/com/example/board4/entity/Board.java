package com.example.board4.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="BOARD")
public class Board {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "NO")
	    private Long no;

	    @Column(name = "WRITER_NO")
	    private Long writerNo;

	    @Column(name = "TITLE")
	    private String title;

	    @Column(name = "CONTENT")
	    private String content;

	    @Column(name = "TYPE")
	    private String type;

	    @Column(name = "ORIGINAL_FILENAME")
	    private String originalFilename;

	    @Column(name = "RENAMED_FILENAME")
	    private String renamedFilename;

	    @Column(name = "READCOUNT")
	    private Integer readCount;

	    @Column(name = "STATUS")
	    private String status;

	    @Column(name = "CREATE_DATE")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date createDate;

	    @Column(name = "MODIFY_DATE")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date modifyDate;
	

}