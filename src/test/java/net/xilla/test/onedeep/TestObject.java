package net.xilla.test.onedeep;

import lombok.Getter;
import net.xilla.boot.reflection.annotation.JsonManager;

@JsonManager(fileName = "data/test-storage.json")
public class TestObject {

    @Getter
    private String id;

    @Getter
    private String name;

    @Getter
    private Double price;

    @Getter
    private String description;

    public TestObject(String id, String name, Double price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public TestObject() {}

}
