package net.bossmannchristoph.lucidsearchtoolkit.explorertools;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ExplorerWindows implements IExplorer {

    private static final Logger LOGGER = LogManager.getLogger(ExplorerWindows.class.getName());
    private ActiveXComponent shell = null;
    private ActiveXComponent window = null;

    private static ExplorerWindows instance = null;
    public static ExplorerWindows getInstance() {
        if (instance == null) {
            instance = new ExplorerWindows();
            instance.init();
        }
        return instance;
    }

    private ExplorerWindows() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Code to execute on shutdown
            LOGGER.log(Level.INFO, "Shutting down...");
            ComThread.Release();
            LOGGER.log(Level.INFO,"ComThread released .. finished!");

        }));
    }

    public void init() {
        ComThread.InitMTA();
        shell = new ActiveXComponent("Shell.Application");
        LOGGER.log(Level.INFO, "ExplorerWindowsTool initialized!");
    }

    @Override
    public void navigate(String path) {
        try {
            LOGGER.log(Level.INFO, "Navigate to path: " + path + " ...");
            if(window == null) {
                LOGGER.log(Level.DEBUG, "Open new window for navigation ...");
                Optional<Dispatch> optionalDispatch = openNewWindow(path);
                optionalDispatch.ifPresent(d -> window = new ActiveXComponent(d));


            }
            else {
                LOGGER.log(Level.DEBUG, "Navigate in existing window with HWND: " +
                        Dispatch.get(window, "HWND"));
                window.invoke("Navigate", path);
            }
            Thread.sleep(500);
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void select(String objectName) {
        if(window == null) {
            throw new IllegalStateException("Cannot select object if no window reference is preset. Call navigate first!");
        }
        LOGGER.info("select folder name: " + window.getProperty("LocationName"));
        Dispatch document = window.getProperty("Document").toDispatch();
        Dispatch folder = Dispatch.get(document, "Folder").toDispatch();

        //Dispatch.invoke(document, "ShellExecute", Dispatch.Method, new Object[] { objectName}, new int[0]);

        Dispatch items = Dispatch.call(folder, "Items").toDispatch();
        EnumVariant enumVariant = new EnumVariant(items);
        Iterator<Variant> it = enumVariant.iterator();
        boolean found = false;
        while(it.hasNext()) {

            Dispatch item = it.next().getDispatch();
            String name = Dispatch.get(item, "Name").toString();
            if (name.equals(objectName)) {
                // Select the file item to highlight it
                //HIER: https://learn.microsoft.com/de-de/windows/win32/shell/shellfolderview-selectitem
                Dispatch.call(document, "SelectItem", item, 13); //13=1+4+8 Wähle element, deselektiere andere Elemente und passe Ansicht an
                LOGGER.debug("object found for selection!");
                found = true;
                break;
            }
        }
        if(!found) {
            LOGGER.warn("Object '" + objectName + "' could not be found!");
        }
    }

    @Override
    public void navigateAndSelect(String pathString) {
        Path path = Paths.get(pathString);
        Path parentPath = path.getParent();
        Iterator<Path> it = path.iterator();
        Path last = null;
        while(it.hasNext()) {
            last = it.next();
        }
        if(last == null) {
            throw new IllegalStateException("Invalid path: " + path);
        }
        navigate(parentPath.toString());
        select(last.toString());

    }

    @Override
    public void openFile(String path) {
        try {
            File f = new File(path);
            String c = "cmd.exe /c start \"\" \"" + f.getAbsolutePath() + "\"";
            LOGGER.info("Open file: " + c);
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(c);

        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(LOGGER.isEnabled(Level.INFO));
        ExplorerWindows explorerWindowsTool = getInstance();
        System.out.println("Get open windows: ");
        explorerWindowsTool.getOpenWindows().forEach(x -> System.out.print(Dispatch.get(x, "HWND").toString() + " "));
        System.out.println();
        explorerWindowsTool.getOpenWindows().forEach(x -> System.out.print(Dispatch.get(x, "HWND").toString() + " "));
        System.out.println();

        explorerWindowsTool.navigate("C:\\users");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //explorerWindowsTool.openFile("C:\\Users\\chris\\Music\\sorted\\BTS\\BE\\dynamite.mp3");
        explorerWindowsTool.openFile("C:\\Users\\chris\\Music\\sorted\\Zoe Wees\\Girls Like Us\\01 Zoe Wees - Girls Like Us.mp3");
        explorerWindowsTool.navigate("C:\\Users\\chris");
        explorerWindowsTool.select("Documents");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        explorerWindowsTool.navigate("C:\\Users\\chris\\Videos");
        explorerWindowsTool.select("Kroatien_2023");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        explorerWindowsTool.navigateAndSelect("C:\\Users\\chris\\Music\\sorted\\Danza Kuduro");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        explorerWindowsTool.navigateAndSelect("C:\\Users\\chris\\Music\\sorted\\Zoe Wees");

        /*explorerWindowsTool.navigate("C:\\Users\\chris\\OneDrive\\Bilder\\Connichi 2019");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
    }

        public Set<Dispatch> getOpenWindows() {
            Set<Dispatch> windows = new HashSet<>();
            Variant variant = shell.invoke("Windows");
            Dispatch dispatch = variant.toDispatch();
            int count = Dispatch.get(dispatch, "Count").getInt();
            LOGGER.log(Level.DEBUG, "Number of open windows " + count);
            EnumVariant enumVariant = new EnumVariant(dispatch);
            while (enumVariant.hasMoreElements()) {
                Variant windowVariant = enumVariant.nextElement();
                windows.add(windowVariant.toDispatch());
            }
            return windows;
        }

        public Optional<Dispatch> openNewWindow(String path) throws InterruptedException {
            Set<Dispatch> beforeWindows = new TreeSet<>(dispatchComparator);
            beforeWindows.addAll(getOpenWindows());
            shell.invoke("Open", path);
            Thread.sleep(1000);
            Set<Dispatch> afterWindows = new TreeSet<>(dispatchComparator);
            afterWindows.addAll(getOpenWindows());
            afterWindows.removeAll(beforeWindows);
            Dispatch foundWindow = null;
            if(afterWindows.size() != 1) {
                LOGGER.info("new window could not be found -> get window with same url");
                for (Dispatch x : beforeWindows) {
                    String dispatchPath = Dispatch.get(x, "LocationURL").toString();
                    if (Paths.get(dispatchPath.substring(8)).equals(Paths.get(path))) {
                        foundWindow = x;
                        break;
                    }
                }
                if (foundWindow == null) {
                    LOGGER.warn("Window could not be retrieved!");
                    return Optional.empty();
                }
            }
            else {
                foundWindow = afterWindows.iterator().next();
            }
            LOGGER.debug("Found window with HWND: " + Dispatch.get(foundWindow, "HWND").toString());
            return Optional.of(foundWindow);
        }

        public static Comparator<Dispatch> dispatchComparator = (d1, d2) -> {
            int hwnd1 = Integer.parseInt(Dispatch.get(d1, "HWND").toString());
            int hwnd2 = Integer.parseInt(Dispatch.get(d2, "HWND").toString());
            return Integer.compare(hwnd1, hwnd2);
        };

}
