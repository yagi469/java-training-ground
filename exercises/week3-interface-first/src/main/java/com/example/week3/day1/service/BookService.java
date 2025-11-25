package com.example.week3.day1.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.dto.BookSearchRequest;
import com.example.week3.day1.entity.Book;
import com.example.week3.day1.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return toResponse(book);
    }

    public BookResponse createBook(BookRequest request) {
        Book book = toEntity(request);
        Book savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        updateEntity(book, request);
        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    // === 新規メソッド（Day 2で追加） ===

    public List<BookResponse> searchByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> searchByPublishedYear(int year) {
        return bookRepository.findByPublishedYearGreaterThanEqual(year)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<BookResponse> searchBooks(BookSearchRequest request) {
        // ページング情報の取得（デフォルト値を設定）
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        // 著者名と出版年の両方が指定されている場合
        if (request.getAuthor() != null && request.getPublishedYearFrom() != null) {
            return bookRepository.findByAuthorContainingAndPublishedYearGreaterThanEqual(
                    request.getAuthor(),
                    request.getPublishedYearFrom(),
                    pageable)
                    .map(this::toResponse);
        }

        // TODO: 他の検索パターンも実装可能
        // 今回はシンプルに全件取得
        return bookRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public BookResponse updateStock(Long id, int quantity) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        book.setStockQuantity(quantity);
        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
    }

    // ヘルパーメソッド: EntityをDTOに変換
    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getStockQuantity());
    }

    // ヘルパーメソッド: DTOをEntityに変換（新規作成用）
    private Book toEntity(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setStockQuantity(request.getStockQuantity());
        return book;
    }

    // ヘルパーメソッド: 既存のEntityを更新
    private void updateEntity(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setStockQuantity(request.getStockQuantity());
    }
}
