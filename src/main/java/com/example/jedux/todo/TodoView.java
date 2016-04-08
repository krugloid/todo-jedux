package com.example.jedux.todo;

import android.content.Context;
import android.util.Pair;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.jedux.todo.State.Task;

import static trikita.anvil.DSL.*;

import trikita.anvil.RenderableAdapter;
import trikita.anvil.RenderableView;
import trikita.jedux.Action;

public class TodoView extends RenderableView {

    private final RenderableAdapter mTaskAdapter = new TaskAdapter();

    private String mInput = "";

    public TodoView(Context c) {
        super(c);
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            linearLayout(() -> {
                size(FILL, WRAP);

                editText(() -> {
                    size(0, WRAP);
                    weight(1);
                    text(mInput);
                    imeOptions(EditorInfo.IME_ACTION_DONE);
                    singleLine(true);
                    onEditorAction((v, actionId, keyEvent) -> {
                        if (actionId == EditorInfo.IME_ACTION_DONE && mInput.trim().length() > 0) {
                            App.dispatch(new Action<>(ActionType.ADD, mInput));
                            mInput = "";
                        }
                        return false;
                    });
                    onTextChanged(s -> {
                        mInput = s.toString();
                    });
                });
            });

            button(() -> {
                size(WRAP, WRAP);
                text("CLEAR CHECKED");
                visibility(App.state().hasChecked());
                onClick(v -> App.dispatch(new Action<>(ActionType.DELETE_CHECKED)));
            });

            listView(() -> {
                size(FILL, WRAP);
                weight(1);
                adapter(mTaskAdapter);
                mTaskAdapter.notifyDataSetChanged();
            });
        });
    }

    private class TaskAdapter extends RenderableAdapter {
        @Override
        public int getCount() {
            return App.state().tasks().size();
        }

        @Override
        public Task getItem(int pos) {
            return App.state().tasks().get(pos);
        }

        @Override
        public void view(int pos) {
            linearLayout(() -> {
                size(FILL, WRAP);
                gravity(Gravity.CENTER_VERTICAL);

                checkBox(() -> {
                    size(0, WRAP);
                    weight(1);
                    padding(dip(10), 0, 0, 0);
                    text("#"+getItem(pos).id()+": "+getItem(pos).name());
                    onCheckedChange((CompoundButton btn, boolean isChecked) -> {
                        App.dispatch(new Action<>(ActionType.TOGGLE, new Pair(getItem(pos).id(), isChecked)));
                    });
                    checked(getItem(pos).checked());
                });

                button(() -> {
                    size(WRAP, WRAP);
                    text("DELETE");
                    onClick(v -> App.dispatch(new Action<>(ActionType.DELETE, getItem(pos).id())));
                });
            });
        }
    }
}

