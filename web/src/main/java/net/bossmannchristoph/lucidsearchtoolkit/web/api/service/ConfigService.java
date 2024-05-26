package net.bossmannchristoph.lucidsearchtoolkit.web.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConfigService {
    public static final Logger LOGGER = LogManager.getLogger(ConfigService.class);

    // Create the file on the server
    String directory = "serverfiles";
    File serverFile = Paths.get(directory, "searchproviderconfig.json").toFile();
    File serverFileOld = Paths.get(directory, "searchproviderconfig.json.old").toFile();

    ObjectMapper objectMapper;

    public ConfigService() {
        objectMapper = new ObjectMapper();
        try {
            Files.createDirectories(Paths.get(directory));
        }
        catch(Exception e) {
            throw new RuntimeException("Error while creating path structure on server!");
        }
    }
    public JsonNode getConfig() {
        try {
            return objectMapper.readTree(serverFile);
        }
        catch(IOException e) {
            throw new RuntimeException("Configuration not available or invalid!", e);
        }
    }

    public void storeConfig(byte[] fileContent) {

        try {
            if (serverFileOld.exists() && !serverFileOld.delete()) {
                throw new RuntimeException("Could not delete old config!");
            }
            if (serverFile.exists()) {
                if (!serverFile.renameTo(serverFileOld)) {
                    throw new RuntimeException("Unknown error while renaming config file on server!");
                }
            }
            if(!serverFile.createNewFile()) {
                throw new RuntimeException("Unknown error while creating new config file on server!");
            }

            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(fileContent);
            }
        } catch (Exception e) {
            if(serverFileOld.renameTo(serverFile)) {
                LOGGER.info("Restore old config WAS successful!");
            }
            else {
                LOGGER.error("Restore old config WAS NOT successful!");
            }
            throw new RuntimeException(e);
        }
    }
}
