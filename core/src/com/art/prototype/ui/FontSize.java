package com.art.prototype.ui;

import lombok.Getter;

@Getter
public enum FontSize {
    SIZE_22(22),
    SIZE_24(24),
    SIZE_28(28),
    SIZE_80(80),
    ;

    private int size;
    FontSize(int size) {
        this.size = size;
    }
}
