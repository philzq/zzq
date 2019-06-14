package zzq.service.impl;

import lombok.Builder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import zzq.entity.Book;
import zzq.service.BookService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class BookServiceImpl implements BookService {

    private static List<Book> books = new ArrayList<>();

    static{
        for(int i=0;i<20;i++){
            books.add(Book.builder().id(i).title("book--"+i).build());
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Book update(Book book) {
        for(Book book1 : books){
            if(book1.getId().equals(book.getId())){
                book1.setTitle(book.getTitle());
                return book;
            }
        }
        return null;
    }

    @Override
    public Book findBookById(Integer id) {
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(Book book1 : books){
            if(book1.getId().equals(id)){
                return book1;
            }
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        Iterator<Book> iterator = books.iterator();
        while(iterator.hasNext()){
            if(iterator.next().getId().equals(id)){
                iterator.remove();
            }
        }
        return true;
    }

    @Override
    public boolean deleteAll() {
        books.clear();
        return true;
    }
}
