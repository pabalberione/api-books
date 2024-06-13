package com.api_books.controller;

import com.api_books.model.Books;
import com.api_books.repository.IBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/")
public class BooksController {

    @Autowired
    private IBooksRepository iBooksRepository;

    //Crear un nuevo libro
    @PostMapping
    public Books createBook(@RequestBody Books books){
        return iBooksRepository.save(books);
    }

    //Obtener todos los libros
    @GetMapping
    public List<Books>getAllBooks(){
        return iBooksRepository.findAll();
    }

    //Obtener un libro por Id
    @GetMapping("{id}")
    public ResponseEntity<Books> getAllBooks(@PathVariable Integer id){
        Books book = iBooksRepository.findById(id).orElse(null);
        if(book == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    //Actualizar un libro
    @PutMapping("{id}")
    public ResponseEntity<Books>updateBookById(@PathVariable Integer id, @RequestBody Books bookDetails){
        Books book = iBooksRepository.findById(id).orElse(null);
        if(book == null){
            return ResponseEntity.notFound().build();
        }
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublishedDate(bookDetails.getPublishedDate());
        Books newBook = iBooksRepository.save(book);
        return ResponseEntity.ok(newBook);
    }

    //Eliminar un libro
    @DeleteMapping("{id}")
    public ResponseEntity<Books>deleteBookById(@PathVariable Integer id){
        Books book = iBooksRepository.findById(id).orElse(null);
        if(book == null){
            return ResponseEntity.notFound().build();
        }
        iBooksRepository.delete(book);
        return ResponseEntity.noContent().build();
    }
}
