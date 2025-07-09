package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Calendar implements Serializable {
    @Id
    private long serviceId;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;

    @NotNull
    private Date starDate;
    private Date endDate;

    //getter and setter
 }
