package com.karelherink.flickrsearch;

public class SearchModeInfo {

    private String displayText;
    private String param;

    public SearchModeInfo(String displayText, String param) {
        this.displayText = displayText;
        this.param = param;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
