package cz.mg.xcursorfactory.event;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public @Component class UserMouseClickListener implements UserListener, MouseListener {
    private final @Mandatory Handler handler;

    public UserMouseClickListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseClicked(@Mandatory MouseEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    public interface Handler {
        void run(MouseEvent event);
    }
}
