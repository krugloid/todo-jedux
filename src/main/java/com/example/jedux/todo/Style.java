package com.example.jedux.todo;

import android.graphics.Color;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;

public final class Style {

    public static class Base {
        public final static int background = R.color.window_background;
        public final static int textSize = sip(18);
        public final static String textFont = "Roboto-Light.ttf";
        public final static String iconFont = "MaterialIcons-Regular.ttf";
        public final static int textColor = Color.BLACK;
        public final static int textInverseColor = Color.WHITE;
        public final static int iconColor = R.color.text_light_gray;
        public final static int iconInverseColor = textInverseColor;
        public final static int margin = dip(5);
        public final static int padding = margin * 2;
    }

    private static void textNormal() {
        textSize(Base.textSize);
        textColor(Base.textColor);
        typeface(Base.textFont);
        backgroundColor(0);
    }

    private static void textInverse() {
        textNormal();
        textColor(Base.textInverseColor);
    }

    private static void iconNormal() {
        textSize(Base.textSize);
        textColor(Base.iconColor);
        typeface(Base.iconFont);
    }

    public static void deleteButton(Anvil.Renderable r) {
        textView(() -> {
            size(dip(36), dip(36));
            gravity(Gravity.CENTER);
            margin(Base.margin);
            text(R.string.clear_icon);
            iconNormal();
            r.view();
        });
    }

    public static void windowBackground(Anvil.Renderable r) {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);
            backgroundResource(Base.background);
            r.view();
        });
    }

    public static void bottomBar(Anvil.Renderable r) {
        button(() -> {
            size(FILL, dip(64));
            layoutGravity(Gravity.BOTTOM);
            text(R.string.delete_icon);
            textSize(Base.textSize * 1.5f);
            textColor(Base.iconInverseColor);
            typeface(Base.iconFont);
            backgroundResource(R.drawable.bottom_bar_background);
            r.view();
        });
    }

    public static class Input {
        public static void layout(Anvil.Renderable r) {
            linearLayout(() -> {
                size(FILL, dip(54));
                margin(Base.margin * 3);
                gravity(Gravity.CENTER_VERTICAL);
                backgroundResource(R.drawable.input_background);
                r.view();
            });
        }

        public static void text(Anvil.Renderable r) {
            editText(() -> {
                size(0, WRAP);
                weight(1);
                padding(Base.padding);
                hint(R.string.todo_input_hint);
                imeOptions(EditorInfo.IME_ACTION_DONE);
                singleLine(true);
                textNormal();
                r.view();
            });
        }
    }

    public static class Card {
        public static void layout(Anvil.Renderable r) {
            linearLayout(() -> {
                size(FILL, WRAP);
                gravity(Gravity.CENTER_VERTICAL);
                margin(Base.margin * 3, Base.margin);
                backgroundResource(R.drawable.bg_card);
                r.view();
            });
        }

        public static void checkbox(Anvil.Renderable r) {
            checkBox(() -> {
                size(0, WRAP);
                weight(1);
                margin(Base.margin * 2, Base.margin * 5);
                padding(Base.padding, 0, 0, 0);
                textInverse();
                r.view();
            });
        }

        public static void delete(Anvil.Renderable r) {
            deleteButton(() -> {
                textColor(Base.iconInverseColor);
                r.view();
            });
        }
    }
}
