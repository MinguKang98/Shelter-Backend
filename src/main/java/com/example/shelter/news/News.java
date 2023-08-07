package com.example.shelter.news;

import com.example.shelter.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "news")
@NoArgsConstructor
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @Column
    private LocalDateTime publishTime;

    @Column
    private String writer;

    @Column
    private String title;

    @Lob
    @Column
    private String content;

    @Column
    private boolean isDeleted = false;

    @Builder
    public News(Long id, LocalDateTime publishTime, String writer, String title, String content) {
        this.id = id;
        this.publishTime = publishTime;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public void updateDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
