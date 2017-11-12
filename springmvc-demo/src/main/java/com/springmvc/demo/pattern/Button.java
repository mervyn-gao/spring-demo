package com.springmvc.demo.pattern;

/**
 * Created by mengran.gao on 2017/11/11.
 */
public class Button {

    private OnClickListener clickListener;

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void click() {
        clickListener.onclick();
    }
}
