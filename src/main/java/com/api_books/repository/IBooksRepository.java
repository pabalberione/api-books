package com.api_books.repository;

import com.api_books.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBooksRepository extends JpaRepository<Books,Integer> {
}
