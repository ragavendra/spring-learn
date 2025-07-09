package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class GtfsTime implements Serializable {
    private long tripId;

    @NotNull
    private long routeId;
    private int directionId;
    private int vehicleId;
    private long stopId;
    private Date arrival;

    //getter and setter
 }
