package cz.mg.xcursorfactory.event;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public @Component class UserMouseWheelListener implements UserListener, MouseWheelListener {
    private final @Mandatory Handler handler;

    public UserMouseWheelListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseWheelMoved(@Mandatory MouseWheelEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    public interface Handler {
        void run(@Mandatory MouseWheelEvent event);
    }
}
