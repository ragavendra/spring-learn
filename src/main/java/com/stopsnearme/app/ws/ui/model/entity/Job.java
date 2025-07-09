package com.stopsnearme.app.ws.ui.model.entity;

import java.io.Serializable;

import org.springframework.web.context.annotation.RequestScope;

import com.stopsnearme.app.ws.ui.model.entity.types.IJob;
import com.stopsnearme.app.ws.ui.model.entity.types.Status;
import com.stopsnearme.app.ws.ui.model.entity.types.Type;

// import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@RequestScope
public class Job implements Serializable, IJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Type type;
    private Status  status;

    //getter and setter

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }
 }
