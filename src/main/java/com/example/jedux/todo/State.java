package com.example.jedux.todo;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import trikita.jedux.Action;
import trikita.jedux.Store;

@Value.Immutable
@Gson.TypeAdapters
public abstract class State {

    @Value.Immutable
    @Gson.TypeAdapters
    public static abstract class Task {
        abstract int id();
        abstract String name();
        abstract boolean checked();
    }

    abstract List<Task> tasks();

    static State getDefault() {
        return ImmutableState.builder().build();
    }

    static class Reducer implements Store.Reducer<Action, State> {

        public State reduce(Action action, State old) {
            ActionType type = (ActionType) action.type;
            switch (type) {
                case ADD:
                    int newId = 1;
                    if (old.tasks().size() > 0) {
                        newId = old.tasks().get(old.tasks().size()-1).id() + 1;
                    }
                    return ImmutableState.builder().from(old).addTasks(newTask((String) action.value, newId)).build();
                case TOGGLE:
                    Pair<Integer, Boolean> p = (Pair<Integer, Boolean>) action.value;
                    return ImmutableState.builder().tasks(toggleTask(old.tasks(), p.first, p.second)).build();
                case DELETE:
                    return ImmutableState.builder().tasks(deleteTask(old.tasks(), (Integer) action.value)).build();
                case DELETE_CHECKED:
                    return ImmutableState.builder().tasks(deleteChecked(old.tasks())).build();
                default:
                    return old;
            }
        }

        private Task newTask(String name, int id) {
            return ImmutableTask.builder().id(id).name(name).checked(false).build();
        }

        private List<Task> toggleTask(List<Task> tasks, int id, boolean checked) {
            List<Task> copy = new ArrayList<>();
            for (Task t : tasks) {
                if (t.id() == id) {
                    copy.add(ImmutableTask.builder()
                                .id(t.id())
                                .name(t.name())
                                .checked(checked)
                                .build());
                } else {
                    copy.add(t);
                }
            }
            return copy;

        }

        private List<Task> deleteChecked(List<Task> tasks) {
            List<Task> copy = new ArrayList<>();
            for (Task t : tasks) {
                if (!t.checked()) {
                    copy.add(t);
                }
            }
            return copy;
        }

        private List<Task> deleteTask(List<Task> tasks, int id) {
            List<Task> copy = new ArrayList<>(tasks);
            for (Task t : copy) {
                if (t.id() == id) {
                    copy.remove(t);
                    break;
                }
            }
            return copy;
        }
    }
}
