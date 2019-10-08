package com.github.appreciated.designer.theme.css.style;

public class CssStyle {
    private String value;
    String name;
    String docUrl;

    public CssStyle(String name, String value, String docUrl) {
        this.name = name;
        this.value = value;
        this.docUrl = docUrl;
    }


    public CssStyle(String name) {
        this.name = name;
        this.value = name;
    }

    public CssStyle(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public CssStyle() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CssStyle) {
            if (((CssStyle) obj).name == null && name == null) {
                return true;
            } else if (name == null) {
                return false;
            }
            return ((CssStyle) obj).name.equals(name);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
