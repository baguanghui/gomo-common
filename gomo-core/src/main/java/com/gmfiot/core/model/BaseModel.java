package com.gmfiot.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author BaGuangHui
 */
public abstract class BaseModel implements Serializable {
    private Long id;

    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}