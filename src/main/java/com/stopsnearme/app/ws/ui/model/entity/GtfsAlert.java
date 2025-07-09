package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GtfsAlert implements Serializable {
    @Id
    private Long id;
    private String header;
    private String description;
    private int cause;
    private int effect;
    private int severity;
    private Date startDate;
    private Date endDate;

    //getter and setter
 }
