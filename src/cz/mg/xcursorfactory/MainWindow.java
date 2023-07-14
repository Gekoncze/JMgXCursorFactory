package cz.mg.xcursorfactory;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.panel.Panel;
import cz.mg.panel.settings.Alignment;
import cz.mg.xcursorfactory.event.UserActionListener;
import cz.mg.xcursorfactory.event.UserChangeListener;
import cz.mg.xcursorfactory.event.UserMouseClickListener;
import cz.mg.xcursorfactory.event.UserMouseWheelListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public @Component class MainWindow extends JFrame {
    private static final @Mandatory String NAME = "JMgXCursorFactory";
    private static final @Mandatory String TITLE = NAME + " " + Version.getInstance();
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;
    private static final int BORDER = 8;
    private static final int PADDING = 8;

    private final @Mandatory JFileChooser openDialog = new JFileChooser();
    private final @Mandatory JFileChooser saveDialog = new JFileChooser();
    private final @Mandatory JTextField path = new JTextField();
    private final @Mandatory ImagePanel image = new ImagePanel();
    private final @Mandatory JSpinner xSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    private final @Mandatory JSpinner ySpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

    private boolean spinnerLock;

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        Panel pathPanel = new Panel(0, PADDING);
        pathPanel.addHorizontal(new JLabel("Image"));
        pathPanel.addHorizontal(path, 1, 0);
        pathPanel.addHorizontal(createButton("...", this::open));
        path.setEditable(false);

        Panel xPanel = new Panel(0, PADDING);
        xPanel.addHorizontal(new JLabel("X"));
        xSpinner.setPreferredSize(new Dimension(64, 24));
        xSpinner.addChangeListener(new UserChangeListener(this::spinnerPositionToImagePosition));
        xPanel.addHorizontal(xSpinner);

        Panel yPanel = new Panel(0, PADDING);
        yPanel.addHorizontal(new JLabel("Y"));
        ySpinner.setPreferredSize(new Dimension(64, 24));
        ySpinner.addChangeListener(new UserChangeListener(this::spinnerPositionToImagePosition));
        yPanel.addHorizontal(ySpinner);

        Panel settingsPanel = new Panel(0, PADDING);
        settingsPanel.addVertical(new JLabel("settings"));
        settingsPanel.addVertical(xPanel);
        settingsPanel.addVertical(yPanel);

        Panel imagePanel = new Panel(0, PADDING);
        imagePanel.addHorizontal(settingsPanel);
        imagePanel.addHorizontal(image, 1, 1);
        image.addMouseListener(new UserMouseClickListener(this::imagePositionToSpinnerPosition));
        image.addMouseWheelListener(new UserMouseWheelListener(this::zoom));

        Panel savePanel = new Panel(0, PADDING, Alignment.RIGHT);
        savePanel.addHorizontal(createButton("Save", this::save));

        Panel verticalPanel = new Panel(BORDER, PADDING);
        verticalPanel.addVertical(pathPanel, 1, 0);
        verticalPanel.addVertical(imagePanel, 1, 1);
        verticalPanel.addVertical(savePanel, 1, 0);

        getContentPane().add(verticalPanel);
    }

    private void imagePositionToSpinnerPosition(@Mandatory MouseEvent event) {
        if (!spinnerLock) {
            try {
                spinnerLock = true;
                xSpinner.setValue(image.getPointerX());
                ySpinner.setValue(image.getPointerY());
            } finally {
                spinnerLock = false;
            }
        }
    }

    private void spinnerPositionToImagePosition() {
        if (!spinnerLock) {
            try {
                spinnerLock = true;
                image.setPointerX((Integer) xSpinner.getValue());
                image.setPointerY((Integer) ySpinner.getValue());
            } finally {
                spinnerLock = false;
            }
        }
    }

    private void zoom(@Mandatory MouseWheelEvent event) {
        int direction = event.getWheelRotation() > 0 ? -1 : 1;
        image.setZoom(image.getZoom() + direction);
    }

    private void open() {
        openDialog.setDialogTitle("Open file");
        openDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openDialog.showOpenDialog(this);
        File file = openDialog.getSelectedFile();
        if (file != null) {
            path.setText(file.getAbsolutePath());
            load();
        }
    }

    private void load() {
        try {
            image.setImage(ImageIO.read(new File(path.getText())));
        } catch (IOException e) {
            throw new RuntimeException("Could not load image '" + path.getText() + "'.", e);
        }
    }

    private void save() {
        if (image.getImage() == null) {
            throw new IllegalArgumentException("No image opened.");
        }

        if (image.getImage().getWidth(this) != image.getImage().getHeight(this)) {
            throw new IllegalArgumentException("Image width and height must be the same.");
        }

        if (path.getText().contains(" ")) {
            throw new IllegalArgumentException("Path to source image cannot contain spaces.");
        }

        saveDialog.setDialogTitle("Save file");
        saveDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        saveDialog.showSaveDialog(this);
        File file = saveDialog.getSelectedFile();
        if (file != null) {
            XCursorFactory.getInstance().create(
                image.getImage().getWidth(this),
                (Integer) xSpinner.getValue(),
                (Integer) ySpinner.getValue(),
                Path.of(path.getText()),
                file.toPath()
            );
        }
    }

    private @Mandatory JButton createButton(@Mandatory String text, @Mandatory UserActionListener.Handler handler) {
        JButton button = new JButton(text);
        button.addActionListener(new UserActionListener(handler));
        return button;
    }

    public static void main(String[] args) {
        new MainWindow().setVisible(true);
    }
}
