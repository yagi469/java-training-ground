package com.example.week3.day4.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.entity.Book;
import com.example.week3.day1.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodBookService {

    private final BookRepository bookRepository;

    // TODO 1: get() を getAllBooks() に改名し、Stream APIで実装
    public List<BookResponse> getAllBooks() {
        // ヒント: return bookRepository.findAll().stream().map(...).collect(...);
        return null;
    }

    // TODO 2: getById() を getBookById() に改名し、カスタム例外を使用
    public BookResponse getBookById(Long id) {
        // ヒント:
        // Book book = bookRepository.findById(id)
        // .orElseThrow(() -> new BookNotFoundException("Book not found with id: " +
        // id));
        // return toResponse(book);
        return null;
    }

    // TODO 3: create() を createBook() に改名し、ヘルパーメソッドを使用
    public BookResponse createBook(BookRequest request) {
        // ヒント:
        // Book book = toEntity(request);
        // Book savedBook = bookRepository.save(book);
        // return toResponse(savedBook);
        return null;
    }

    // TODO 4: search() を searchBooks() に改名し、Stream APIでフィルタリング
    public List<BookResponse> searchBooks(String author, Integer yearFrom) {
        // ヒント:
        // return bookRepository.findAll()
        // .stream()
        // .filter(book -> matchesAuthor(book, author))
        // .filter(book -> matchesYear(book, yearFrom))
        // .map(this::toResponse)
        // .collect(Collectors.toList());
        return null;
    }

    // TODO 5: 条件チェック用のヘルパーメソッド
    private boolean matchesAuthor(Book book, String author) {
        // ヒント: return author == null || book.getAuthor().contains(author);
        return false;
    }

    private boolean matchesYear(Book book, Integer yearFrom) {
        // ヒント: return yearFrom == null || book.getPublishedYear() >= yearFrom;
        return false;
    }

    // TODO 6: Entity → DTO変換のヘルパーメソッド
    private BookResponse toResponse(Book book) {
        // ヒント: return new BookResponse(...);
        return null;
    }

    // TODO 7: DTO → Entity変換のヘルパーメソッド
    private Book toEntity(BookRequest request) {
        // ヒント:
        // Book book = new Book();
        // book.setTitle(request.getTitle());
        // ...
        // return book;
        return null;
    }
}
