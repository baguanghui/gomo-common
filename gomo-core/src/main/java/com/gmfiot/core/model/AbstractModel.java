package com.gmfiot.core.model;

import java.util.Date;

/**
 * @author ThinkPad
 */
public abstract class AbstractModel extends BaseModel {
    private Date lastModifiedAt;

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
