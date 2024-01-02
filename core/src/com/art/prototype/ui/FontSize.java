package com.art.prototype.ui;

import lombok.Getter;

@Getter
public enum FontSize {
    SIZE_22(22),
    SIZE_24(24),
    SIZE_28(28),
    SIZE_34(34),
    SIZE_40(40),
    ;

    private int size;
    FontSize(int size) {
        this.size = size;
    }
}
