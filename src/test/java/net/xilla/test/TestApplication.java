package net.xilla.test;

import net.xilla.boot.XillaAPI;
import net.xilla.boot.XillaApplication;
import net.xilla.boot.api.program.StartupPriority;
import net.xilla.boot.api.program.StartupProcess;
import net.xilla.boot.storage.manager.Manager;
import net.xilla.test.onedeep.TestObject;

public class TestApplication {

    public static void start() {
        Manager<TestObject> manager = XillaAPI.getManager(TestObject.class);
        System.out.println("Manager " + manager);
        System.out.println("Data" + manager.keySet());
        for(String key : manager.keySet()) {
            System.out.println("Object ID " + manager.get(key).getId());
            System.out.println("Object Name " + manager.get(key).getName());
            System.out.println("Object Price " + manager.get(key).getPrice());
            System.out.println("Object Desc " + manager.get(key).getDescription());
        }
    }

    public static void main(String[] args) {
        XillaApplication.create("Test Application");
        XillaApplication.getInstance().registerStartupProcess(new StartupProcess("Test Application", StartupPriority.COMPLETE) {
            @Override
            public void run() {
                start();
            }
        });
        XillaApplication.startProgram();
    }

}
