package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import base.BaseClass;

public class ScreenshotUtil {

    public static String takeScreenshot(String name) {
        try {
            File folder = new File("snapshots");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            TakesScreenshot ts = (TakesScreenshot) BaseClass.driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String path = "snapshots/" + name + "_" + timestamp + ".png";

            FileHandler.copy(src, new File(path));
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
