package net.bossmannchristoph.lucidsearchtoolkit.web.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class ExplorerController {

    @RequestMapping(value = "openfolder", method = RequestMethod.GET)
    public String openFolder(@RequestParam String path) {
        try {
            osOpenFolder(path);
            return "Browser successfully opened!!";
        }
        catch(Exception e) {
            e.printStackTrace();
            return "ERROR while opening browser!";
        }
    }

    @RequestMapping(value = "openfolderselectfile", method = RequestMethod.GET)
    public String openFolderSelectFile(@RequestParam String path) {
        String absolutePath = Paths.get(path).toAbsolutePath().toString();
        System.out.println("openfolderselectfile absolute path: " +  absolutePath);
        try {
            osOpenFolderAndSelectObject(path);
            //osOpenFolderAndSelectObject("C:\\Users\\chris");
            return "Browser successfully opened and selected file!!";
        }
        catch(Exception e) {
            e.printStackTrace();
            return "ERROR while opening browser and selecting file!";
        }
    }

    public static void osOpenFolder(String path) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String command = "powershell.exe -command \"Start-Process explorer.exe -ArgumentList '" + path + "' -WindowStyle Maximized\"";
            Process process = Runtime.getRuntime().exec(command);
            long pid = process.pid();
            System.out.println("Process id: " + pid);

        }
        else {
            throw new UnsupportedOperationException("Unsupported operating system");
        }
    }

    public static void osOpenFolderAndSelectObject(String path) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String command = "powershell.exe \"Start-Process explorer.exe -ArgumentList @('/select,', '" + path + "') -WindowStyle Maximized\"";
            System.out.println("Print command: " + command);
            Runtime.getRuntime().exec(command);
        } else {
            throw new UnsupportedOperationException("Unsupported operating system");
        }
    }
}
