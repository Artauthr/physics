package com.art.prototype.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class ADialog extends Table {
    private Table content;

    public ADialog () {
        this.content = buildContent(content);

    }

    private void initDarkBackground () {

    }


    abstract Table buildContent (Table content);

}
