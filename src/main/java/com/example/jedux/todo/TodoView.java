package com.example.jedux.todo;

import android.content.Context;
import android.util.Pair;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;

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
        mTaskAdapter.notifyDataSetChanged();

        frameLayout(() -> {
            Style.windowBackground(() -> {
                // task name input
                Style.Input.layout(() -> {
                    Style.Input.text(() -> {
                        text(mInput);
                        onTextChanged(s -> mInput = s.toString());
                        onEditorAction((v, actionId, keyEvent) -> {
                            if (actionId == EditorInfo.IME_ACTION_DONE && mInput.trim().length() > 0) {
                                App.dispatch(new Action<>(ActionType.ADD, mInput));
                                mInput = "";
                            }
                            return false;
                        });
                    });

                    // clear input button
                    Style.deleteButton(() -> onClick(v -> mInput = ""));
                });

                // task list
                listView(() -> {
                    size(FILL, WRAP);
                    weight(1);
                    divider(null);
                    adapter(mTaskAdapter);
                });
            });

            // clear checked tasks button
            Style.bottomBar(() -> {
                visibility(App.state().hasChecked());
                onClick(v -> App.dispatch(new Action<>(ActionType.DELETE_CHECKED)));
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
            Style.Card.layout(() -> {

                Style.Card.checkbox(() -> {
                    text("#"+getItem(pos).id()+": "+getItem(pos).name());
                    onCheckedChange((CompoundButton btn, boolean isChecked) -> {
                        App.dispatch(new Action<>(ActionType.TOGGLE, new Pair<>(getItem(pos).id(), isChecked)));
                    });
                    checked(getItem(pos).checked());
                });

                Style.Card.delete(() -> onClick(v -> App.dispatch(new Action<>(ActionType.DELETE, getItem(pos).id()))));
            });
        }
    }
}

