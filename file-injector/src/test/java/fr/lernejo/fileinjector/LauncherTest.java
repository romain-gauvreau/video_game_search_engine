package fr.lernejo.fileinjector;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void main_terminates_before_5_sec() {
        assertTimeoutPreemptively(
            Duration.ofSeconds(5L),
            () -> Launcher.main(new String[]{}));
    }

    @Test
    void should_send_file_to_queue() {
        File file = new File("src/test/resources");
        String jsonTestFile = file.getAbsolutePath()+"/games.json";
        assertTimeoutPreemptively(
            Duration.ofSeconds(10L),
            () -> Launcher.main(new String[]{jsonTestFile}));
    }
}
