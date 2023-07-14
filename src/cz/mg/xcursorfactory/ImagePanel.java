package cz.mg.xcursorfactory;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.xcursorfactory.event.UserMouseClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public @Component class ImagePanel extends JPanel {
    private static final int BLOCK_SIZE = 16;
    private static final int POINTER_SIZE = 8;

    private @Optional Image image;
    private int pointerX;
    private int pointerY;
    private int zoom = 1;

    public ImagePanel() {
        addMouseListener(new UserMouseClickListener(this::onMouseClicked));
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public int getPointerX() {
        return pointerX;
    }

    public void setPointerX(int pointerX) {
        this.pointerX = pointerX;
        repaint();
    }

    public int getPointerY() {
        return pointerY;
    }

    public void setPointerY(int pointerY) {
        this.pointerY = pointerY;
        repaint();
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = Math.max(1, Math.min(16, zoom));
        repaint();
    }

    private void onMouseClicked(@Mandatory MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            pointerX = event.getX() / zoom;
            pointerY = event.getY() / zoom;
            repaint();
        }
    }

    @Override
    protected void paintComponent(@Mandatory Graphics g) {
        drawBackground(g);
        scaleGraphics((Graphics2D)g);
        drawImage(g);
        drawPointer(g);
    }

    private void scaleGraphics(@Mandatory Graphics2D g) {
        g.scale(zoom, zoom);
    }

    private void drawBackground(@Mandatory Graphics g) {
        boolean dark = false;
        for (int y = 0; y < getHeight(); y += BLOCK_SIZE) {
            dark = !dark;
            boolean light = dark;
            for (int x = 0; x < getWidth(); x += BLOCK_SIZE) {
                light = !light;
                g.setColor(light ? Color.LIGHT_GRAY : Color.GRAY);
                g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    private void drawImage(@Mandatory Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    private void drawPointer(@Mandatory Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(pointerX - POINTER_SIZE, pointerY, pointerX + POINTER_SIZE, pointerY);
        g.drawLine(pointerX, pointerY - POINTER_SIZE, pointerX, pointerY + POINTER_SIZE);
    }
}
