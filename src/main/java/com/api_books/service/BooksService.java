package com.api_books.service;

import com.api_books.model.Books;
import com.api_books.repository.IBooksRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    @Autowired
    IBooksRepository iBooksRepository;

    //Create book
    public Books saveBooks(Books books){
        return iBooksRepository.save(books);
    }

    //Get all books
    public List<Books>getAllBooks(){
        return iBooksRepository.findAll();
    }

    //Get book for Id
    public Optional<Books> getBookById(Integer id){
        return iBooksRepository.findById(id);
    }

    //Update book
    public Books updateBook(Integer id, Books bookDetails){
        Optional<Books> actualBook = iBooksRepository.findById(id);
        if(actualBook.isPresent()){
            Books nuevoBook = actualBook.get();
            nuevoBook.setAuthor(bookDetails.getAuthor());
            nuevoBook.setIsbn(bookDetails.getIsbn());
            nuevoBook.setPublishedDate(bookDetails.getPublishedDate());
            return iBooksRepository.save(nuevoBook);
        }else {
            return null;
        }
    }

    //Delete book
    public void deletBook(Integer id){
    Books book = iBooksRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    iBooksRepository.delete(book);
        }

}
