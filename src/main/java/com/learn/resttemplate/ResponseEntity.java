package com.learn.resttemplate;

public class ResponseEntity {
    private String result;
    private String errorMessage;
    private Data data;
    private String returnCode;
    private String taskID;

    private Success[] success;
    private Fail[] fail;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Success[] getSuccess() {
        return success;
    }

    public void setSuccess(Success[] success) {
        this.success = success;
    }

    public Fail[] getFail() {
        return fail;
    }

    public void setFail(Fail[] fail) {
        this.fail = fail;
    }
}
