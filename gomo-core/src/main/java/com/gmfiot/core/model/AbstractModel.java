package com.gmfiot.core.model;

import java.util.Date;

/**
 * @author BaGuangHui
 */
public abstract class AbstractModel extends BaseModel {
    private Date lastModifiedAt;

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Override
    public String toString() {
        return "AbstractModel{" +
                "lastModifiedAt=" + lastModifiedAt +
                "} " + super.toString();
    }
}
