/* Copyright © 2015 Oracle and/or its affiliates. All rights reserved. */
package com.example.spring.rest;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
           
    public Book findByIsbn(@Param("isbn") String isbn);
    
    @RestResource(path="getBooksByAuthor")
    public List<Book> findByAuthorIgnoreCaseOrderByTitleAsc(@Param("author") String author);
    
    @Query("SELECT b FROM Book b WHERE b.published BETWEEN :startYear AND :endYear ORDER BY b.published")
    public List<Book> getBooksBetweenYears(@Param("startYear")int startYear, @Param("endYear")int endYear);
    
    public List<Book> findByTitleContaining(@Param("title") String title); 
   
    public int countByAuthor(@Param("author") String author);
}
