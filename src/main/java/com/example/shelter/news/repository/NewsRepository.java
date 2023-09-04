package com.example.shelter.news.repository;

import com.example.shelter.news.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("select n from News n where n.id = :id and n.isDeleted = false")
    Optional<News> findByIdNotDeleted(@Param("id") Long id);

    @Query("select n from News n where n.isDeleted = false ")
    Page<News> findAllNotDeleted(Pageable pageable);

    @Query("""
            select n from News n
            where n.id < :id and
            n.isDeleted = false
            order by n.id desc
            """)
    List<News> findPrevByIdNotDeleted(@Param("id") Long id, Pageable pageable);

    @Query("""
            select n from News n
            where n.id > :id and
            n.isDeleted = false
            order by n.id asc
            """)
    List<News> findNextByIdNotDeleted(@Param("id") Long id, Pageable pageable);

    @Query("select n from News n where n.newsCode = :code and n.isDeleted = false")
    Optional<News> findByNewsCodeNotDeleted(@Param("code") String newsCode);

}
