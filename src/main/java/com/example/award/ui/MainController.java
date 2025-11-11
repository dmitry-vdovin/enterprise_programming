package com.example.award.ui;

import com.example.award.model.Gift;
import com.example.award.model.GiftCatalog;
import com.example.award.service.CalcService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class MainController {
    // чекбоксы поздравителей
    @FXML private CheckBox cbA, cbB, cbC;
    @FXML private ComboBox<Gift> giftBox;

    // концерт
    @FXML private RadioButton rbConcertYes, rbConcertNo;

    // постоянный клиент
    @FXML private CheckBox cbLoyal;

    // вывод
    @FXML private Label totalLabel;
    @FXML private TextField orderField;

    private final GiftCatalog catalog = new GiftCatalog();
    private final CalcService calc = new CalcService(8000);

    @FXML
    private void initialize() {
        // по умолчанию выбран Поздравитель A и "Концерт: нет"
        cbA.setSelected(true);
        rbConcertNo.setSelected(true);

        // загрузить список подарков под текущего поздравителя
        refreshGiftsForCurrentGreeter();

        // навесим обработчики на все элементы
        List<CheckBox> greeterCbs = List.of(cbA, cbB, cbC);
        for (CheckBox cb : greeterCbs) {
            cb.setOnAction(e -> {
                // реализуем "в группе чекбоксов — выбрать только один"
                if (cb.isSelected()) greeterCbs.stream().filter(x -> x != cb).forEach(x -> x.setSelected(false));
                else cb.setSelected(true); // не даём остаться без выбора
                refreshGiftsForCurrentGreeter();
                recalc();
            });
        }

        giftBox.setOnAction(e -> recalc());
        rbConcertYes.setOnAction(e -> recalc());
        rbConcertNo.setOnAction(e -> recalc());
        cbLoyal.setOnAction(e -> recalc());

        recalc();
    }

    private String currentGreeter() {
        if (cbA.isSelected()) return "Поздравитель A";
        if (cbB.isSelected()) return "Поздравитель B";
        return "Поздравитель C";
    }

    private void refreshGiftsForCurrentGreeter() {
        giftBox.getItems().setAll(catalog.giftsFor(currentGreeter()));
        if (!giftBox.getItems().isEmpty()) giftBox.getSelectionModel().selectFirst();
    }

    private void recalc() {
        Gift gift = giftBox.getSelectionModel().getSelectedItem();
        boolean withConcert = rbConcertYes.isSelected();
        boolean loyal = cbLoyal.isSelected();

        double total = calc.total(gift, withConcert, loyal);
        totalLabel.setText("Стоимость: " + (long) total + " ₽");

        String order = currentGreeter() + "; " +
                (gift == null ? "подарок не выбран" : gift.name()) + "; " +
                "концерт: " + (withConcert ? "да" : "нет") + "; " +
                (loyal ? "постоянный клиент (-10%)" : "без скидки");
        orderField.setText(order);
    }
}