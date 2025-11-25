package com.example.week3.day1.service;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.entity.Book;
import com.example.week3.day1.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_書籍が存在する場合_全書籍のリストが返る() {
        // given (準備)
        // テストデータを作成
        // Book型のリストを作成し、3冊分のBookオブジェクトを追加
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", "Author 1", "ISBN 1", 2020, 10),
                new Book(2L, "Book 2", "Author 2", "ISBN 2", 2021, 20),
                new Book(3L, "Book 3", "Author 3", "ISBN 3", 2022, 30));
        // when(bookRepository.findAll()).thenReturn(...)でモックの振る舞いを定義
        when(bookRepository.findAll()).thenReturn(books);

        // when (実行)
        // bookService.getAllBooks() を呼び出し
        List<BookResponse> result = bookService.getAllBooks();

        // then (検証)
        // 結果が3件であることを確認
        assertEquals(3, result.size());
        // 各BookResponseの内容が正しいことを確認
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals("ISBN 1", result.get(0).getIsbn());
        assertEquals(2020, result.get(0).getPublishedYear());
        assertEquals(10, result.get(0).getStockQuantity());
    }

    @Test
    void getAllBooks_書籍が0冊の場合_空のリストが返る() {
        // given
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<BookResponse> result = bookService.getAllBooks();

        // then
        assertEquals(0, result.size());
    }

    @Test
    void getBookById_存在するIDを指定_書籍が返る() {
        // 実装してみてください
        // ヒント:
        // 1. Book オブジェクトを作成
        Book book = new Book(1L, "Book 1", "Author 1", "ISBN 1", 2020, 10);
        // 2. when(bookRepository.findById(1L)).thenReturn(Optional.of(book))
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        // 3. BookResponse result = bookService.getBookById(1L)
        BookResponse result = bookService.getBookById(1L);
        // 4. assertNotNull(result)
        assertNotNull(result);
        // 5. assertEquals("期待値", result.getTitle())
        assertEquals("Book 1", result.getTitle());
    }

    @Test
    void getBookById_存在しないIDを指定_例外がスローされる() {
        // 実装してみてください
        // ヒント:
        // 1. when(bookRepository.findById(999L)).thenReturn(Optional.empty())
        // 2. assertThrows(RuntimeException.class, () -> bookService.getBookById(999L))
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.getBookById(999L));
    }

    @Test
    void createBook_正しいリクエスト_書籍が作成される() {
        // 実装してみてください
        // ヒント:
        // 1. BookRequest を作成
        BookRequest request = new BookRequest("Book 1", "Author 1", "ISBN 1", 2020, 10);
        // 2. Book を作成（保存後の状態、IDが設定されている）
        Book book = new Book(1L, "Book 1", "Author 1", "ISBN 1", 2020, 10);
        // 3. when(bookRepository.save(any(Book.class))).thenReturn(book)
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        // 4. BookResponse result = bookService.createBook(request)
        BookResponse result = bookService.createBook(request);
        // 5. assertEquals(期待値, result.getTitle())
        assertEquals("Book 1", result.getTitle());
    }

    @Test
    void searchByAuthor_著者名で検索_該当書籍のリストが返る() {
        // Day 2の機能のテスト
        // ヒント:
        // 1. Book のリストを作成（著者名が一致するもの）
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", "山田太郎", "ISBN 1", 2020, 10),
                new Book(2L, "Book 2", "山田太郎", "ISBN 2", 2021, 20));
        // 2. when(bookRepository.findByAuthorContaining("太郎")).thenReturn(books)
        when(bookRepository.findByAuthorContaining("太郎")).thenReturn(books);
        // 3. List<BookResponse> result = bookService.searchByAuthor("太郎")
        List<BookResponse> result = bookService.searchByAuthor("太郎");
        // 4. assertEquals(期待件数, result.size())
        assertEquals(2, result.size());
        assertEquals("山田太郎", result.get(0).getAuthor());
        assertEquals("山田太郎", result.get(1).getAuthor());
    }
}
