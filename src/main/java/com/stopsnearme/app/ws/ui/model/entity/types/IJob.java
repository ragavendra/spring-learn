package com.stopsnearme.app.ws.ui.model.entity.types;

public interface IJob {
    public Type getType();
    public void setType(Type type);
    public Status getStatus();
    public void setStatus(Status status);
    public Long getId();
}
