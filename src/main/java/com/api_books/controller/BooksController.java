package com.api_books.controller;

import com.api_books.model.Books;
import com.api_books.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/")
public class BooksController {

    @Autowired
    private BooksService booksService;

    //Crear un nuevo Libro
    @PostMapping
    public Books addBook(@RequestBody Books books){
        return booksService.saveBooks(books);
    }

    //Obtener todos los libros
    @GetMapping
    public List<Books>getAllBooks(){
        return booksService.getAllBooks();
    }

    //Obtener Libro por Id
    @GetMapping("{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id){
        return booksService.getBookById(id).map(books -> ResponseEntity.ok().body(books))
                .orElse(ResponseEntity.notFound().build());
    }


    //Modificar un libro
    @PutMapping("{id}")
    public ResponseEntity<Books> updateBookById(@PathVariable Integer id, @RequestBody Books booksDetails){
        Books actualBook = booksService.updateBook(id, booksDetails);
        if(actualBook == null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(actualBook);
        }
    }

    //Eliminar un libro
    @DeleteMapping("{id}")
    public ResponseEntity<Void>deleteBook(@PathVariable Integer id){
        try {
            booksService.deletBook(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
