package com.tracker.dynamic.menu;

public class MenuElements {

    private String  elementName;
    private String elementTitle;
    private String searchParams;
    private String userRoles;


    public MenuElements(String elementName, String elementTitle, String searchParams, String userRoles) {
        this.elementName = elementName;
        this.elementTitle = elementTitle;
        this.searchParams = searchParams;
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

    public String getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(String searchParams) {
        this.searchParams = searchParams;
    }
}
