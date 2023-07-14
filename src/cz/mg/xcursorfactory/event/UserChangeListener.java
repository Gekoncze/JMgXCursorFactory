package cz.mg.xcursorfactory.event;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public @Component class UserChangeListener implements UserListener, ChangeListener {
    private final @Mandatory Handler handler;

    public UserChangeListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        handleExceptions(handler::run);
    }

    public interface Handler {
        void run();
    }
}
