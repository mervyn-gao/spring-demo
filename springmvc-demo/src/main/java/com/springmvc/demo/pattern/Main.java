package com.springmvc.demo.pattern;

/**
 * Created by mengran.gao on 2017/11/11.
 */
public class Main {

    public static void main(String[] args) {
        Button button = new Button();
        button.setClickListener(new MyListener());
        button.click();
    }
}
