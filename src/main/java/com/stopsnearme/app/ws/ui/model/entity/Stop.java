package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Stop implements Serializable {
    @Id
    private Long stopId;
    private int stopCode;
    private String stopName;
    private String stopDescription;
    private double latitude;
    private double longitude;
    private int zoneId;
    private String stopUrl;
    private int locationType;
    private int parentStation;

    //getter and setter
    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public void setStopCode(int stopCode) {
        this.stopCode = stopCode;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public void setStopDescription(String stopDescription) {
        this.stopDescription = stopDescription;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public void setStopUrl(String stopUrl) {
        this.stopUrl = stopUrl;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public void setParentStation(int parentStation) {
        this.parentStation = parentStation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stop{");
        sb.append("stopId=").append(stopId);
        sb.append(", stopCode=").append(stopCode);
        sb.append(", stopName=").append(stopName);
        sb.append(", stopDescription=").append(stopDescription);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", zoneId=").append(zoneId);
        sb.append(", stopUrl=").append(stopUrl);
        sb.append(", locationType=").append(locationType);
        sb.append(", parentStation=").append(parentStation);
        sb.append('}');
        return sb.toString();
    }

    public Long getStopId() {
        return stopId;
    }

    public int getStopCode() {
        return stopCode;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopDescription() {
        return stopDescription;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getZoneId() {
        return zoneId;
    }

    public String getStopUrl() {
        return stopUrl;
    }

    public int getLocationType() {
        return locationType;
    }

    public int getParentStation() {
        return parentStation;
    }
 }
