package net.bossmannchristoph.lucidsearchtoolkit.explorertools;

import com.jacob.com.Dispatch;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;

import java.util.ArrayList;
import java.util.List;

public class LittleTests {

    public static void main2(String[] args) {
        User32 user32 = User32.INSTANCE;
        List<WinDef.HWND> windows = new ArrayList<>();

        user32.EnumWindows((hwnd, lParam) -> {
            windows.add(hwnd);
            return true; // Continue enumeration
        }, null);

        for (WinDef.HWND hWnd : windows) {
            char[] buffer = new char[1024];
            user32.GetWindowText(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            user32.GetClassName(hWnd, buffer, buffer.length);
            String className = Native.toString(buffer);
            if (className.contains("CabinetWClass") || className.contains("ExplorerWClass")) {
                System.out.println("Window handle: " + hWnd + ", Title: " + windowTitle + ", ClassName: " + className);
            }

        }
    }

        public static void main(String[] args) {
            ComThread.InitMTA(); // Initialize COM thread
            ActiveXComponent window = null;
            try {
                ActiveXComponent shell = new ActiveXComponent("Shell.Application");
                Dispatch windows = shell.invoke("Windows").toDispatch();
                EnumVariant enumVariant = new EnumVariant(windows);
                int counter = 0;
                boolean found = false;
                while (enumVariant.hasMoreElements()) {
                    counter++;
                    Variant windowVariant = enumVariant.nextElement();
                    window = new ActiveXComponent(windowVariant.toDispatch());
                    String windowName = window.getPropertyAsString("Name");
                    if (windowName != null && windowName.equals("File Explorer") && !found) {
                        // Found the File Explorer window, you can perform actions here
                        String locationName = window.getPropertyAsString("LocationName");
                        System.out.println("Found File Explorer window.");
                        System.out.println(locationName);
                        window.invoke("Navigate", "C:\\Users\\chris\\Music\\temp\\2018-01-05");
                        found = true;
                    }
                    
                }
                System.out.println("number of windows: " + counter);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                window.invoke("Navigate", "C:\\Users\\chris\\Videos");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } finally {
                ComThread.Release(); // Release COM thread
            }
            //window.invoke("Navigate", "C:\\Users\\chris\\Downloads");
            //ComThread.Release();
        }
}
