package com.vitaliy.springCourse.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {

    private int id;

    @NotEmpty(message = "Book title should not be empty")
    @Size(min = 2, max = 100, message = "Book title should be from 2 to 100 characters")
    private String bookTitle;

    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 100, message = "Author name should be from 2 to 100 characters")
    private String author;

    @Min(value = 1500, message = "Year of publication should greater than 1500")
    private int yearOfPublication;

    public Book(int id, String bookTitle, String author, int yearOfPublication) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public Book() {

    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookTitle='" + bookTitle + '\'' +
                ", author='" + author + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                '}';
    }
}
