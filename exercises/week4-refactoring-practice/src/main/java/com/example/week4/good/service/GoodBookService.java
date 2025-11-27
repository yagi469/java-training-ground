package com.example.week4.good.service;

import com.example.week4.common.dto.BookRequest;
import com.example.week4.common.dto.BookResponse;
import com.example.week4.common.entity.Book;
import com.example.week4.common.exception.BookNotFoundException;
import com.example.week4.common.repository.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodBookService {

    private final BookRepository bookRepository;

    // get() を getAllBooks() に改名し、Stream APIで実装
    public List<BookResponse> getAllBooks() {
        // ヒント: return bookRepository.findAll().stream().map(...).collect(...);
        return bookRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // getById() を getBookById() に改名し、カスタム例外を使用
    public BookResponse getBookById(@NonNull Long id) {
        // ヒント:
        // Book book = bookRepository.findById(id)
        // .orElseThrow(() -> new BookNotFoundException("Book not found with id: " +
        // id));
        // return toResponse(book);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return toResponse(book);
    }

    // create() を createBook() に改名し、ヘルパーメソッドを使用
    public BookResponse createBook(BookRequest request) {
        // ヒント:
        // Book book = toEntity(request);
        @NonNull
        Book book = toEntity(request);
        // Book savedBook = bookRepository.save(book);
        Book savedBook = bookRepository.save(book);
        // return toResponse(savedBook);
        return toResponse(savedBook);
    }

    // search() を searchBooks() に改名し、Stream APIでフィルタリング
    public List<BookResponse> searchBooks(String author, Integer yearFrom) {
        // ヒント:
        // return bookRepository.findAll()
        // .stream()
        // .filter(book -> matchesAuthor(book, author))
        // .filter(book -> matchesYear(book, yearFrom))
        // .map(this::toResponse)
        // .collect(Collectors.toList());
        return bookRepository.findAll()
                .stream()
                .filter(book -> matchesAuthor(book, author))
                .filter(book -> matchesYear(book, yearFrom))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 書籍更新メソッド
    public BookResponse updateBook(@NonNull Long id, BookRequest request) {
        // ヒント:
        // Book book = bookRepository.findById(id)
        // .orElseThrow(() -> new BookNotFoundException("Book not found with id: " +
        // id));
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        // book.setTitle(request.getTitle());
        book.setTitle(request.getTitle());
        // book.setAuthor(request.getAuthor());
        book.setAuthor(request.getAuthor());
        // book.setIsbn(request.getIsbn());
        book.setIsbn(request.getIsbn());
        // book.setPublishedYear(request.getPublishedYear());
        book.setPublishedYear(request.getPublishedYear());
        // book.setStockQuantity(request.getStockQuantity());
        book.setStockQuantity(request.getStockQuantity());
        // Book updatedBook = bookRepository.save(book);
        Book updatedBook = bookRepository.save(book);
        // return toResponse(updatedBook);
        return toResponse(updatedBook);
    }

    // 書籍削除メソッド
    public void deleteBook(@NonNull Long id) {
        // ヒント:
        // if (!bookRepository.existsById(id)) {
        // throw new BookNotFoundException("Book not found with id: " + id);
        // }
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        // bookRepository.deleteById(id);
        bookRepository.deleteById(id);
    }

    // 条件チェック用のヘルパーメソッド
    private boolean matchesAuthor(Book book, String author) {
        // ヒント: return author == null || book.getAuthor().contains(author);
        return author == null || book.getAuthor().contains(author);
    }

    private boolean matchesYear(Book book, Integer yearFrom) {
        // ヒント: return yearFrom == null || book.getPublishedYear() >= yearFrom;
        return yearFrom == null || book.getPublishedYear() >= yearFrom;
    }

    // Entity → DTO変換のヘルパーメソッド
    private BookResponse toResponse(@NonNull Book book) {
        // ヒント: return new BookResponse(...);
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getStockQuantity());
    }

    // DTO → Entity変換のヘルパーメソッド
    private @NonNull Book toEntity(BookRequest request) {
        // ヒント:
        // Book book = new Book();
        // book.setTitle(request.getTitle());
        // ...
        // return book;
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setStockQuantity(request.getStockQuantity());
        return book;
    }
}
