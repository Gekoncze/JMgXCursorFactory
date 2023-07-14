package cz.mg.xcursorfactory;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public @Service class XCursorFactory {
    private static volatile @Service XCursorFactory instance;

    public static @Service XCursorFactory getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new XCursorFactory();
                }
            }
        }
        return instance;
    }

    private XCursorFactory() {
    }

    public void create(int size, int pointerX, int pointerY, @Mandatory Path sourceFile, @Mandatory Path targetFile) {
        String line = size + " " + pointerX + " " + pointerY + " " + sourceFile;
        try {
            ProcessBuilder.startPipeline(List.of(
                new ProcessBuilder("echo", line).redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder("xcursorgen").redirectOutput(targetFile.toFile())
            ));
        } catch (IOException e) {
            throw new RuntimeException("Could not save cursor.", e);
        }
    }
}
