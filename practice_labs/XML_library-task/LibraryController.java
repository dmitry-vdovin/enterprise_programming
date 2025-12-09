package org.example.libraryapp_task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class LibraryController {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, Integer> yearCol;
    @FXML private TableColumn<Book, String> categoryCol;
    @FXML private TableColumn<Book, Double> priceCol;
    @FXML private TableColumn<Book, Integer> totalCol;
    @FXML private TableColumn<Book, Integer> availCol;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private Document xmlDocument;

    @FXML
    public void initialize() {
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        authorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));
        yearCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getYear()));
        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalCopies()));
        availCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAvailableCopies()));

        bookTable.setItems(books);
    }

    /** 3.2.1 Загрузка и вывод всех сведений о книгах */
    @FXML
    private void loadXML() {
        try {
            xmlDocument = XMLUtils.loadDocument("src/main/resources/library.xml", "src/main/resources/library.xsd");
            books.clear();

            NodeList list = xmlDocument.getElementsByTagName("book");
            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);
                books.add(new Book(
                        el.getAttribute("title"),
                        el.getAttribute("author"),
                        Integer.parseInt(el.getAttribute("year")),
                        el.getAttribute("category"),
                        Double.parseDouble(el.getAttribute("price")),
                        Integer.parseInt(el.getAttribute("total")),
                        Integer.parseInt(el.getAttribute("available"))
                ));
            }
            showAlert("Загрузка завершена", "XML успешно проверен и загружен.");
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить XML: " + e.getMessage());
        }
    }

    /** 3.2.2 Добавление новой книги */
    @FXML
    private void addBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Введите данные книги через запятую:");
        dialog.setContentText("Название, Автор, Год, Категория, Цена, Всего, В наличии");
        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] parts = input.split(",");
                Element newBook = xmlDocument.createElement("book");
                newBook.setAttribute("title", parts[0].trim());
                newBook.setAttribute("author", parts[1].trim());
                newBook.setAttribute("year", parts[2].trim());
                newBook.setAttribute("category", parts[3].trim());
                newBook.setAttribute("price", parts[4].trim());
                newBook.setAttribute("total", parts[5].trim());
                newBook.setAttribute("available", parts[6].trim());

                xmlDocument.getDocumentElement().appendChild(newBook);
                XMLUtils.saveDocument(xmlDocument, "src/main/resources/library.xml");
                loadXML();
                showAlert("Успех", "Книга добавлена.");
            } catch (Exception e) {
                showAlert("Ошибка", "Неверный формат ввода.");
            }
        });
    }

    /** 3.2.3 Поиск по автору, году или категории */
    @FXML
    private void searchBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Введите автора, год или категорию для поиска:");
        dialog.showAndWait().ifPresent(input -> {
            String query = input.trim().toLowerCase();
            ObservableList<Book> filtered = books.filtered(b ->
                    b.getAuthor().toLowerCase().contains(query) ||
                            b.getCategory().toLowerCase().contains(query) ||
                            String.valueOf(b.getYear()).contains(query));
            bookTable.setItems(filtered);
        });
        // xpath
    }

    /** 3.2.4 Переоценка книги */
    @FXML
    private void changePrice() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите книгу для изменения цены.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getPrice()));
        dialog.setHeaderText("Введите новую цену:");
        dialog.showAndWait().ifPresent(priceStr -> {
            try {
                double newPrice = Double.parseDouble(priceStr);
                selected.setPrice(newPrice);

                NodeList list = xmlDocument.getElementsByTagName("book");
                for (int i = 0; i < list.getLength(); i++) {
                    Element el = (Element) list.item(i);
                    if (el.getAttribute("title").equals(selected.getTitle())) {
                        el.setAttribute("price", String.valueOf(newPrice));
                        break;
                    }
                }

                XMLUtils.saveDocument(xmlDocument, "src/main/resources/library.xml");
                loadXML();
                showAlert("Успех", "Цена обновлена.");
            } catch (Exception e) {
                showAlert("Ошибка", "Введите корректное число.");
            }
        });
    }

    /** 3.2.5 Эмуляция выдачи книги */
    @FXML
    private void lendBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите книгу для выдачи.");
            return;
        }

        if (selected.getAvailableCopies() <= 0) {
            showAlert("Нет в наличии", "Все экземпляры выданы.");
            return;
        }

        int newCount = selected.getAvailableCopies() - 1;
        selected.setAvailableCopies(newCount);

        NodeList list = xmlDocument.getElementsByTagName("book");
        for (int i = 0; i < list.getLength(); i++) {
            Element el = (Element) list.item(i);
            if (el.getAttribute("title").equals(selected.getTitle())) {
                el.setAttribute("available", String.valueOf(newCount));
                break;
            }
        }

        try {
            XMLUtils.saveDocument(xmlDocument, "src/main/resources/library.xml");
            loadXML();
            showAlert("Выдача книги", "Книга выдана. Осталось экземпляров: " + newCount);
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}