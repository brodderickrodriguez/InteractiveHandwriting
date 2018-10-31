package group6.interactivehandwriting.activities.Room.draw.drawables;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import group6.interactivehandwriting.common.app.actions.ActionId;

/**
 * Created by JakeL on 10/30/18.
 */

public class DrawableManager implements Map<ActionId, Drawable> {
    Map<ActionId, Drawable> table;

    public DrawableManager() {
        super();
        table = new HashMap<>();
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return table.containsValue(value);
    }

    @Override
    public Drawable get(Object key) {
        return table.get(key);
    }

    @Override
    public Drawable put(ActionId key, Drawable value) {
        return table.put(key, value);
    }

    @Override
    public Drawable remove(Object key) {
        return table.remove(key);
    }

    @Override
    public void putAll(@NonNull Map<? extends ActionId, ? extends Drawable> m) {
        table.putAll(m);
    }

    @Override
    public void clear() {
        table.clear();
    }

    @NonNull
    @Override
    public Set<ActionId> keySet() {
        return table.keySet();
    }

    @NonNull
    @Override
    public Collection<Drawable> values() {
        return table.values();
    }

    @NonNull
    @Override
    public Set<Entry<ActionId, Drawable>> entrySet() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else if (o instanceof DrawableManager) {
            return table.values().equals(((DrawableManager) o).values());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return table.hashCode();
    }
}
