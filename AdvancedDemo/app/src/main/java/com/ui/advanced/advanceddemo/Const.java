package com.ui.advanced.advanceddemo;

/**
 * Created by scott on 16-11-22.
 */
public final class Const {
    private Const() {
    }

    // key that bundle used for message between MainActivity and TemplateActivity
    public static final String BUNDLE_LAYOUT = "layout"; // layout id
    public static final String BUNDLE_ID = "id"; // action id
    public static final String BUNDLE_TITLE = "title"; // title

    // data type of home adapter to init recyclerview
    public final static int TYPE_NORML = 0;
    public final static int TYPE_GRID = 1;
    public final static int TYPE_VERTICAL = 2;
    public final static int TYPE_HORIZON = 3;
    // data content of each type
    public final static int[] ARRAYLIST = new int[]{
            R.array.contents,
            R.array.contents_grid,
            R.array.contents_stagger_v,
            R.array.contents_stagger_h
    };

}
