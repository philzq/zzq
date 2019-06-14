package zzq.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import zzq.entity.Book;

import java.util.List;

public interface BookService {

    /**
     * 查詢所有書籍
     * @return
     */
    @Cacheable(cacheNames="BOOK")
    List<Book> findAll();

    /**
     * 每次调用该方法时，都会检查缓存，以查看是否已经执行了调用，并且不需要重复调用
     * @return
     */
    @Cacheable(cacheNames="BOOK",key = "'BOOK'.concat(#id.toString())")
    Book findBookById(Integer id);

    /**
     * 添加書籍
     * 方法总是被执行，它的结果被放入缓存
     * @param book
     * @return
     */
    @CachePut(cacheNames="BOOK",key = "'BOOK'.concat(#book.id.toString())")
    Book update(Book book);

    /**
     * 刪除某個書籍
     * 刪除指定cacheNames--key緩存
     * @param id
     * @return
     */
    @CacheEvict(cacheNames="BOOK",key = "'BOOK'.concat(#id.toString())")
    boolean delete(Integer id);

    /**
     * 刪除所有書籍
     * allEntries=true----刪除cacheNames--所有key
     * @return
     */
    @CacheEvict(cacheNames="BOOK", allEntries=true)
    boolean deleteAll();
}
