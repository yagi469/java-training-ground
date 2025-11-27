package com.example.week3.day4.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.entity.Book;
import com.example.week3.day1.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadBookService {

    private final BookRepository bookRepository;

    // 悪い例1: メソッド名が不明確、for文を使用
    public List<BookResponse> get() {
        List<Book> list = bookRepository.findAll();
        List<BookResponse> res = new ArrayList<>();

        // for文でループ（Stream APIを使うべき）
        for (int i = 0; i < list.size(); i++) {
            Book b = list.get(i);
            BookResponse r = new BookResponse(
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    b.getPublishedYear(),
                    b.getStockQuantity());
            res.add(r);
        }
        return res;
    }

    // 悪い例2: 例外処理が不適切
    public BookResponse getById(Long id) {
        Book b = bookRepository.findById(id).orElse(null);
        if (b == null) {
            throw new RuntimeException("Not found"); // メッセージが不親切
        }
        // Entity → DTOの変換が重複
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getIsbn(),
                b.getPublishedYear(),
                b.getStockQuantity());
    }

    // 悪い例3: 重複コード（変換処理が毎回同じ）
    public BookResponse create(BookRequest req) {
        Book b = new Book();
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setIsbn(req.getIsbn());
        b.setPublishedYear(req.getPublishedYear());
        b.setStockQuantity(req.getStockQuantity());

        Book saved = bookRepository.save(b);

        // また同じ変換処理
        return new BookResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getAuthor(),
                saved.getIsbn(),
                saved.getPublishedYear(),
                saved.getStockQuantity());
    }

    // 悪い例4: メソッドが肥大化、複雑なネスト
    public List<BookResponse> search(String author, Integer year) {
        List<Book> all = bookRepository.findAll();
        List<Book> filtered = new ArrayList<>();

        // ネストしたif文
        for (Book b : all) {
            boolean match = true;
            if (author != null) {
                if (!b.getAuthor().contains(author)) {
                    match = false;
                }
            }
            if (year != null) {
                if (b.getPublishedYear() < year) {
                    match = false;
                }
            }
            if (match) {
                filtered.add(b);
            }
        }

        // また変換処理が重複
        List<BookResponse> result = new ArrayList<>();
        for (Book b : filtered) {
            result.add(new BookResponse(
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    b.getPublishedYear(),
                    b.getStockQuantity()));
        }

        return result;
    }
}
