package com.api_books.controller;

import com.api_books.model.Books;
import com.api_books.repository.IBooksRepository;
import com.api_books.service.BooksService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@WebMvcTest(BooksController.class)
@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BooksService booksService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Books createBooks;
    private Books updateBooks;
    private List<Books> booksList;



    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
        createBooks = objectMapper.readValue(
                Files.readAllBytes(Paths.get("src/test/resources/book-create.json")),Books.class);
        updateBooks = objectMapper.readValue(
                Files.readAllBytes(Paths.get("src/test/resources/book-update.json")),Books.class);
        booksList = objectMapper.readValue(
                Files.readAllBytes(Paths.get("src/test/resources/book-list.json")),
                new TypeReference<List<Books>>() {});
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getBookById_Found() throws Exception{
        when(booksService.getBookById(1)).thenReturn(Optional.of(createBooks));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("Carlin Calvo"))
                .andExpect(jsonPath("$.isbn").value("7368471384"))
                .andExpect(jsonPath("$.publishedDate").value("2023-07-11T11:15:30.000Z"));
    }

    @Test
    void getBookById_NotFound() throws Exception{
        when(booksService.getBookById(1)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBooks() throws Exception {
        when(booksService.saveBooks(any(Books.class))).thenReturn(createBooks);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBooks)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("Carlin Calvo"))
                .andExpect(jsonPath("$.isbn").value("7368471384"))
                .andExpect(jsonPath("$.publishedDate").value("2023-07-11T11:15:30.000Z"));
    }

    @Test
    void getAllBooks() throws Exception{
        when(booksService.getAllBooks()).thenReturn(booksList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].author").value("Carlin Calvo"))
                .andExpect(jsonPath("$[0].isbn").value("7368471384"))
                .andExpect(jsonPath("$[0].publishedDate").value("2023-07-11T11:15:30.000Z"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].author").value("Pedro Aznar"))
                .andExpect(jsonPath("$[1].isbn").value("7368471384"))
                .andExpect(jsonPath("$[1].publishedDate").value("2023-06-11T11:15:30.000Z"));
    }

    @Test
    void testUpdateBook()throws Exception{
        when(booksService.updateBook(anyInt(), any(Books.class))).thenReturn(updateBooks);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBooks)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.author").value("Carlin Calvo Con pelo"))
                .andExpect(jsonPath("$.isbn").value("7368471384"))
                .andExpect(jsonPath("$.publishedDate").value("2023-07-11T11:15:30.000Z"));
    }

    @Test
    void testDeleteBook()throws Exception{
        doNothing().when(booksService).deletBook(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());
    }
}
