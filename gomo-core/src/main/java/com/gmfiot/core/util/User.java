package com.gmfiot.core.util;

import com.gmfiot.core.model.BaseModel;

/**
 * @author ThinkPad
 */
public class User extends BaseModel {
    private String name;
    private Integer status;
    private Integer failures;
    private String referenceId;
    private Integer directory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getDirectory() {
        return directory;
    }

    public void setDirectory(Integer directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", failures=" + failures +
                ", referenceId='" + referenceId + '\'' +
                ", directory=" + directory +
                "} " + super.toString();
    }
}
