package com.tracker.dynamic;

import java.util.List;

public class FrontElement {

    String elementName;
    String elementTitle;
    List enableForRolesList;
    List <FrontElement> subListOfFrontElement;

    public FrontElement(String elementName, String elementTitle, List enableForRolesList, List<FrontElement>  subListOfFrontElement){
        this.elementName = elementName;
        this.elementTitle = elementTitle;
        this.enableForRolesList = enableForRolesList;
        this.subListOfFrontElement = subListOfFrontElement;
    }

    public String getElementName() {
        return elementName;
    }

    private void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementTitle() {
        return elementTitle;
    }

    private void setElementTitle(String elementTitle) {
        this.elementTitle = elementTitle;
    }

    public List getEnableForRolesList() {
        return enableForRolesList;
    }

    private void setEnableForRolesList(List enableForRolesList) {
        this.enableForRolesList = enableForRolesList;
    }

    public List<FrontElement> getSubListOfFrontElement() {
        return subListOfFrontElement;
    }

    private void setSubListOfFrontElement(List<FrontElement> subListOfFrontElement) {
        this.subListOfFrontElement = subListOfFrontElement;
    }
}
