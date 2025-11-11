package com.example.award.model;

public record Gift(String name, double price) {
    @Override public String toString() { return name + " — " + (long)price + " ₽"; }
}