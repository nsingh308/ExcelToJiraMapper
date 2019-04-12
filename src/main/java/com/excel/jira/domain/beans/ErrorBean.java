package com.excel.jira.domain.beans;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class ErrorBean {

    @Expose
    private List<String> errorMessages = new ArrayList<>();
    @Expose
    private ErrorsBean errors;

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
