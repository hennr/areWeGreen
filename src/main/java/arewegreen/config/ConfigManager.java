package arewegreen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemResource;

@Component
public class ConfigManager implements ApplicationListener<ApplicationReadyEvent> {

    private File arewegreenHome;
    private AreWeGreenProperties areWeGreenProperties;
    private static final String layoutJson = "layout.json";
    private static final String applicationProperties = "application.properties";

    @Autowired
    public ConfigManager(Environment environment, AreWeGreenProperties areWeGreenProperties) {
        arewegreenHome = new File(environment.getProperty("user.home") + "/arewegreen/");
        this.areWeGreenProperties = areWeGreenProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (areWeGreenProperties.getCreateDefaultConfigFile() && !configExists()) {
            createDefaultConfig();
        }
    }

    boolean configExists() {
        return Files.exists(arewegreenHome.toPath());
    }

    private void createDefaultConfig() {
        try {
            Files.createDirectory(arewegreenHome.toPath());

            Files.copy(
                    Paths.get(getSystemResource("defaultConfig/" + applicationProperties).toURI()),
                    Paths.get(arewegreenHome + "/" +  applicationProperties)
            );

            Files.copy(
                    Paths.get(getSystemResource("defaultConfig/" + layoutJson).toURI()),
                    Paths.get(getLayoutJsonLocation())
            );

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getLayoutJsonLocation() {
        return arewegreenHome + "/" + layoutJson;
    }
}
