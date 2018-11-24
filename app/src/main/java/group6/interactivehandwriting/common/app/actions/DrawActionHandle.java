package group6.interactivehandwriting.common.app.actions;

import java.util.List;

import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.actions.draw.DrawableAction;

/**
 * Created by JakeL on 10/28/18.
 */

public interface DrawActionHandle {
    public void handleDrawAction(Profile user, DrawableAction action);

    public void undo(Profile profile);

    public List<Action> getActionHistory();

    public void mergeActionHistory(List<Action> actionHistory);
}
