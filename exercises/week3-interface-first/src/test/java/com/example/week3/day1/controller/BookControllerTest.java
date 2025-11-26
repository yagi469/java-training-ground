package com.example.week3.day1.controller;

import com.example.week3.day1.dto.BookRequest;
import com.example.week3.day1.dto.BookResponse;
import com.example.week3.day1.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_GET_200とリストが返る() throws Exception {
        // given
        // BookResponseのリストを作成
        // ヒント: List<BookResponse> books = Arrays.asList(...);
        List<BookResponse> books = Arrays.asList(
                new BookResponse(1L, "タイトル1", "著者1", "ISBN-1", 2023, 10),
                new BookResponse(2L, "タイトル2", "著者2", "ISBN-2", 2024, 20),
                new BookResponse(3L, "タイトル3", "著者3", "ISBN-3", 2025, 30));
        // when(bookService.getAllBooks()).thenReturn(books);
        when(bookService.getAllBooks()).thenReturn(books);
        // when & then
        // mockMvc.perform(get("/books"))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$").isArray())
        // .andExpect(jsonPath("$[0].title").value("期待値"));
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("タイトル1"));
    }

    @Test
    void getBookById_存在するID_200と書籍情報が返る() throws Exception {
        // given
        // BookResponse を作成
        // ヒント: BookResponse book = new BookResponse(1L, "タイトル", ...);
        BookResponse book = new BookResponse(1L, "タイトル", "著者", "ISBN", 2023, 10);
        // when(bookService.getBookById(1L)).thenReturn(book);
        when(bookService.getBookById(1L)).thenReturn(book);
        // when & then
        // mockMvc.perform(get("/books/1"))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$.title").value("期待値"));
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("タイトル"));
    }

    @Test
    void createBook_正しいリクエスト_201が返る() throws Exception {
        // given
        // BookRequest を作成
        // ヒント: BookRequest request = new BookRequest();
        // request.setTitle("新しい本");
        // ...
        BookRequest request = new BookRequest("新しい本", "著者", "ISBN", 2023, 10);
        // BookResponse を作成（作成後のレスポンス）
        BookResponse response = new BookResponse(1L, "新しい本", "著者", "ISBN", 2023, 10);
        // when(bookService.createBook(any(BookRequest.class))).thenReturn(response);
        when(bookService.createBook(any(BookRequest.class))).thenReturn(response);

        // when & then
        // mockMvc.perform(post("/books")
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(objectMapper.writeValueAsString(request)))
        // .andExpect(status().isCreated())
        // .andExpect(jsonPath("$.title").value("期待値"));
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("新しい本"));
    }

    @Test
    void searchByAuthor_著者名を指定_該当書籍が返る() throws Exception {
        // given - Day 2の機能のテスト
        // BookResponse のリストを作成
        List<BookResponse> books = Arrays.asList(
                new BookResponse(1L, "タイトル1", "山田太郎", "ISBN-1", 2023, 10),
                new BookResponse(2L, "タイトル2", "田中太郎", "ISBN-2", 2024, 20),
                new BookResponse(3L, "タイトル3", "佐藤太郎", "ISBN-3", 2025, 30));

        // when(bookService.searchByAuthor("太郎")).thenReturn(books);
        when(bookService.searchByAuthor("太郎")).thenReturn(books);

        // when & then
        // mockMvc.perform(get("/books/search/author").param("author", "太郎"))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$").isArray())
        // .andExpect(jsonPath("$[0].author").value("期待値"));
        mockMvc.perform(get("/books/search/author").param("author", "太郎"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].author").value("山田太郎"));
    }

    @Test
    void updateBook_存在するID_200と更新された書籍が返る() throws Exception {
        // given
        // BookRequest を作成（更新内容）
        BookRequest request = new BookRequest("新しい本", "著者", "ISBN", 2023, 10);
        // BookResponse を作成（更新後のレスポンス）
        BookResponse response = new BookResponse(1L, "新しい本", "著者", "ISBN", 2023, 10);
        // when(bookService.updateBook(eq(1L),
        // any(BookRequest.class))).thenReturn(response);
        when(bookService.updateBook(eq(1L),
                any(BookRequest.class))).thenReturn(response);

        // when & then
        // mockMvc.perform(put("/books/1")
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(objectMapper.writeValueAsString(request)))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$.title").value("期待値"));
        mockMvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("新しい本"));
    }

    @Test
    void deleteBook_存在するID_204が返る() throws Exception {
        // given
        // 削除は戻り値がないので、Serviceのモックは不要

        // when & then
        // mockMvc.perform(delete("/books/1"))
        // .andExpect(status().isNoContent());
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}
