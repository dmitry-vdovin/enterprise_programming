package org.example.libraryapp_task;

public class Book {
    private String title;
    private String author;
    private int year;
    private String category;
    private double price;
    private int totalCopies;
    private int availableCopies;

    public Book(String title, String author, int year, String category, double price, int totalCopies, int availableCopies) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.price = price;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void setPrice(double price) { this.price = price; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    @Override
    public String toString() {
        return String.format("%s (%s) [%d, %s] - %.2f₽, %d/%d",
                title, author, year, category, price, availableCopies, totalCopies);
    }
}