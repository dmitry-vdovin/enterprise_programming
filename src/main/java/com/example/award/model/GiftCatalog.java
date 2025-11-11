package com.example.award.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public final class GiftCatalog {
    private final Map<String, ObservableList<Gift>> byGreeter = new HashMap<>();

    public GiftCatalog() {
        byGreeter.put("Поздравитель A", FXCollections.observableArrayList(
                new Gift("Букет", 1500), new Gift("Сертификат", 3000), new Gift("Кубок", 5000)));
        byGreeter.put("Поздравитель B", FXCollections.observableArrayList(
                new Gift("Букет", 1200), new Gift("Памятная медаль", 3500), new Gift("Кубок", 6000)));
        byGreeter.put("Поздравитель C", FXCollections.observableArrayList(
                new Gift("Букет", 1800), new Gift("Сертификат", 3200), new Gift("Статуэтка", 7000)));
    }

    public Set<String> greeters() { return byGreeter.keySet(); }
    public ObservableList<Gift> giftsFor(String greeter) {
        return byGreeter.getOrDefault(greeter, FXCollections.observableArrayList());
    }
}