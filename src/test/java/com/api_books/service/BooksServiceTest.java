package com.api_books.service;

import com.api_books.model.Books;
import com.api_books.repository.IBooksRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;

import java.awt.print.Book;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BooksServiceTest {

    @Mock
    IBooksRepository iBooksRepository;

    @InjectMocks
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
    void getBookById_Found(){
        //Configurar datos de la prueba
        when(iBooksRepository.findById(1)).thenReturn(Optional.of(createBooks));

        //Llamar al metodo a probar
        Optional<Books>result = booksService.getBookById(1);

        //Verificar resultados
        assertTrue(result.isPresent());
        assertEquals("Carlin Calvo", result.get().getAuthor());
        verify(iBooksRepository, times(1)).findById(1);
    }

    @Test
    void getBookById_Not_Found(){
        when(iBooksRepository.findById(1)).thenReturn(Optional.empty());

        //Llamar al método a probar
        Optional<Books> result = booksService.getBookById(1);

        //Verificar resultados
        assertFalse(result.isPresent());
        verify(iBooksRepository,times(1)).findById(1);
    }

    @Test
    void testSaveBook(){
        Books book = new Books(1, "Carlin Calvo",7368471384L, "2023-07-11T11:15:30.000Z");
        when(iBooksRepository.save(book)).thenReturn(createBooks);

        //Llamar al método a probar
        Books result = booksService.saveBooks(book);

        //Verificar resultados
        assertNotNull(result);
        assertEquals(createBooks.getId(),result.getId());
        assertEquals(createBooks.getAuthor(), result.getAuthor());
        assertEquals(createBooks.getIsbn(),result.getIsbn());
        assertEquals(createBooks.getPublishedDate(),result.getPublishedDate());
        verify(iBooksRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBookNotFound(){
        //Configurar el mock para que findby devuelva un optional vacio
        when(iBooksRepository.findById(1)).thenReturn(Optional.empty());
        //Verificar que se lanza una runtimeException cuando se intenta eliminar un libro que no existe
        assertThrows(RuntimeException.class,()-> booksService.deletBook(1));
    }

    @Test
    void testDeleteBook(){
        //Configurar el mock para que el findby devuelva un registro con un libro
        when(iBooksRepository.findById(1)).thenReturn(Optional.of(createBooks));

        //Ejecutar el método a probar
        booksService.deletBook(1);

        //Verificar que se haya llamado el método delete del repositorio una vez
        verify(iBooksRepository, times(1)).delete(createBooks);
    }
}
