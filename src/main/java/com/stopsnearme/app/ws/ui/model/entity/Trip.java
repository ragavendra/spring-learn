package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Trip implements Serializable {
    @Id
    private Long tripId;
    private int serviceId;
    private int routeId;
    private int shapeId;
    private int directionId;
    private int wheelChairAccessible;
    private int bikesAllowed;
    private String blockId;
    private String tripHeadSign;
    private String tripShortName;

    //getter and setter

    @Override
    public String toString() {
        return "Trip [tripId=" + tripId + ", serviceId=" + serviceId + ", routeId=" + routeId + ", shapeId=" + shapeId
                + ", directionId=" + directionId + ", wheelChairAccessible=" + wheelChairAccessible + ", bikesAllowed="
                + bikesAllowed + ", blockId=" + blockId + ", tripHeadSign=" + tripHeadSign + ", tripShortName="
                + tripShortName + ", toString()=" + super.toString() + "]";
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public Long getTripId() {
        return tripId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getShapeId() {
        return shapeId;
    }

    public int getDirectionId() {
        return directionId;
    }

    public int getWheelChairAccessible() {
        return wheelChairAccessible;
    }

    public int getBikesAllowed() {
        return bikesAllowed;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getTripHeadSign() {
        return tripHeadSign;
    }

    public String getTripShortName() {
        return tripShortName;
    }

    public void setShapeId(int shapeId) {
        this.shapeId = shapeId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public void setWheelChairAccessible(int wheelChairAccessible) {
        this.wheelChairAccessible = wheelChairAccessible;
    }

    public void setBikesAllowed(int bikesAllowed) {
        this.bikesAllowed = bikesAllowed;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public void setTripHeadSign(String tripHeadSign) {
        this.tripHeadSign = tripHeadSign;
    }

    public void setTripShortName(String tripShortName) {
        this.tripShortName = tripShortName;
    }
 }
