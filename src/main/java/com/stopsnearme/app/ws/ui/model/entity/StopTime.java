package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Embeddable;

@Embeddable
public class StopTime implements Serializable {

    private int tripId;
    private int stopId;
    private Date arrivalTime;

    //getter and setter
 }
