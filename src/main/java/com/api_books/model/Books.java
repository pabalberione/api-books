package com.api_books.model;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String author;
    private long isbn;
    private String publishedDate;
}
