package net.xilla.boot;

import lombok.Getter;
import net.xilla.boot.api.program.ProgramManager;
import net.xilla.boot.reflection.ClassScanner;
import net.xilla.boot.reflection.scanner.CacheManagerScanner;
import net.xilla.boot.reflection.scanner.ConfigScanner;
import net.xilla.boot.reflection.scanner.JsonManagerScanner;
import net.xilla.boot.reflection.scanner.ManagerScanner;

public class XillaApplication extends ProgramManager {

    public static void create(String applicationName) {
        XillaApplication xillaApplication = new XillaApplication(applicationName);
        xillaApplication.start();
    }

    public static void startProgram() {
        getInstance().startup();
    }

    @Getter
    private static XillaApplication instance;

    public XillaApplication(String name) {
        super(name);

        ClassScanner.addScan(new ManagerScanner());
        ClassScanner.addScan(new JsonManagerScanner());
        ClassScanner.addScan(new CacheManagerScanner());
        ClassScanner.addScan(new ConfigScanner());

        instance = this;
    }

    public void start() {
        ClassScanner scanner = new ClassScanner(this);
        scanner.load();
    }

}
