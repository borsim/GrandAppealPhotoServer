package com.shaunthegoat.spring.rest;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
           
    /*public Image findByIsbn(@Param("isbn") String isbn);
    
    @RestResource(path="getBooksByAuthor")
    public List<Image> findByAuthorIgnoreCaseOrderByTitleAsc(@Param("author") String author);
    
    @Query("SELECT b FROM Book b WHERE b.published BETWEEN :startYear AND :endYear ORDER BY b.published")
    public List<Image> getBooksBetweenYears(@Param("startYear")int startYear, @Param("endYear")int endYear);
    
    public List<Image> findByTitleContaining(@Param("title") String title); 
   
    public int countByAuthor(@Param("author") String author);*/
}
