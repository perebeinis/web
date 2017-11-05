package com.tracker.dynamic.header;

public class HeaderElements {

    private String  elementName;
    private String elementTitle;
    private String userRoles;

    public HeaderElements(String elementName, String elementTitle, String userRoles) {
        this.elementName = elementName;
        this.elementTitle = elementTitle;
        this.userRoles = userRoles;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementTitle() {
        return elementTitle;
    }

    public void setElementTitle(String elementTitle) {
        this.elementTitle = elementTitle;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }
}
