package game.controller;

@FunctionalInterface
public interface Action {

    /**
     * perfom an action, no parameters or return value.
     */
    void perform();
}
