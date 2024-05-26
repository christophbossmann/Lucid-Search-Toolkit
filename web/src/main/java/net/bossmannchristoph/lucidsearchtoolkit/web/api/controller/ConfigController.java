package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.Response;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.model.ResponseMessage;
import net.bossmannchristoph.lucidsearchtoolkit.web.api.service.ConfigService;
import net.bossmannchristoph.lucidsearchtoolkit.web.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

@RestController
public class ConfigController {

    //Logger LOGGER = LogManager.getLogger(ConfigController.class);

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "api/searchprovider/uploadconfig", method = RequestMethod.POST, produces = "application/json")

    public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file) {
        // Define the directory where the file will be stored
        try {
            configService.storeConfig(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(new ResponseMessage(
                "Successfully updated config file!", "UPDATE_CONFIGFILE_SUCCESSFUL",
                HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "api/searchprovider/getconfig", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ConfigResponse> getConfig()  {

        return new ResponseEntity<>(new ConfigResponse(configService.getConfig(),
                "GET_CONFIGFILE_SUCCESSFUL", HttpStatus.OK.value()), HttpStatus.OK);
    }

    public static class ConfigResponse extends Response {

        private final JsonNode config;

        public ConfigResponse(JsonNode config, String status, int statuscode) {
            super(status, statuscode);
            this.config = config;
        }

        public JsonNode getConfig() {
            return config;
        }
    }
}
