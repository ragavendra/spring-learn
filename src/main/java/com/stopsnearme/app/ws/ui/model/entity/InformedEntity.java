package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class InformedEntity implements Serializable {
    private Long id;
    private String agencyId;
    private String routeId;
    private int routeType;
    private int stopId;
    private String direcionId;

    //getter and setter
 }
