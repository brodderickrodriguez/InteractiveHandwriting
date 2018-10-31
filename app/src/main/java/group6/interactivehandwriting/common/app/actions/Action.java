package group6.interactivehandwriting.common.app.actions;

public abstract class Action {
    protected ActionType type;
    protected ActionId id;

    public abstract ActionType getType();

    public void setId(ActionId id) {
        this.id = id;
    };

    public ActionId getId() {
        return id;
    }
}
