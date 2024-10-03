package com.ufo.cart.module;

public enum Category {
    COMBAT("Combat"), PLAYER("Player"), MISC("Misc"), RENDER("Render"), CLIENT("Client");

    public final String name;
    public boolean expanded;

    Category(String name) {
        this.name = name;
        this.expanded = true;
    }

    public void toggleExpand() {
        this.expanded = !this.expanded;
    }

    public void expand() {
        this.expanded = true;
    }

    public void unexpand() {
        this.expanded = false;
    }
}
