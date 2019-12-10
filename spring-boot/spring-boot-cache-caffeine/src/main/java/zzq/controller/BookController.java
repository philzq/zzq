package zzq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zzq.entity.Book;
import zzq.service.BookService;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("books")
    public List<Book> findAll(){
        return bookService.findAll();
    }

    @GetMapping("books/{id}")
    public Book findBookById(@PathVariable Integer id){
        return bookService.findBookById(id);
    }

    @PutMapping("book")
    public Book update(@RequestBody Book book){
        return bookService.update(book);
    }

    @DeleteMapping("book/{id}")
    public boolean delete(@PathVariable Integer id){
        return bookService.delete(id);
    }

    @DeleteMapping("books")
    public boolean deleteAll(){
        return bookService.deleteAll();
    }
}
