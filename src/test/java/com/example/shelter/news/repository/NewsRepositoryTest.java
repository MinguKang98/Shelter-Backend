package com.example.shelter.news.repository;

import com.example.shelter.news.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NewsRepositoryTest {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    TestEntityManager em;

    @Test
    public void findByIdNotDeleted_존재하는_뉴스_테스트() {
        //given
        News news = News.builder()
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();
        em.persist(news);
        em.flush();
        em.clear();

        ///when
        Optional<News> findNews = newsRepository.findByIdNotDeleted(news.getId());

        //then
        assertThat(findNews).isNotEmpty();
        assertThat(findNews.get().getId()).isEqualTo(news.getId());
    }

    @Test
    public void findByIdNotDeleted_삭제된_뉴스_테스트() {
        //given
        News news = News.builder()
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .build();
        news.updateDeleted(true);
        em.persist(news);
        em.flush();
        em.clear();

        ///when
        Optional<News> findNews = newsRepository.findByIdNotDeleted(news.getId());

        //then
        assertThat(findNews).isEmpty();
    }

    @Test
    public void findByIdNotDeleted_존재하지_않는_뉴스_테스트() {
        //given
        Long id = 1L;

        ///when
        Optional<News> findNews = newsRepository.findByIdNotDeleted(id);

        //then
        assertThat(findNews).isEmpty();
    }

    @Test
    public void findAllNotDeleted_테스트() {
        //given
        for (int i = 0; i < 10; i++) {
            News news = News.builder()
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목")
                    .content("내용")
                    .build();
            em.persist(news);
        }
        em.flush();
        em.clear();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 8);
        Page<News> findNews = newsRepository.findAllNotDeleted(pageRequest);

        //then
        assertThat(findNews.getTotalElements()).isEqualTo(10);
        assertThat(findNews.getTotalPages()).isEqualTo(2);
        assertThat(findNews.getNumber()).isEqualTo(0);
        assertThat(findNews.getSize()).isEqualTo(8);
        assertThat(findNews.getNumberOfElements()).isEqualTo(8);
    }

    @Test
    public void findPrevByIdNotDeleted_이전_존재_테스트() {
        //given
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            News news = News.builder()
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            em.persist(news);
            newsList.add(news);
        }
        em.flush();
        em.clear();
        newsList.sort(Comparator.comparing(News::getTitle));
        Long id = newsList.get(3).getId();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<News> prevNews = newsRepository.findPrevByIdNotDeleted(id, pageRequest);

        //then
        assertThat(prevNews.size()).isEqualTo(1);
        assertThat(prevNews.get(0).getId()).isEqualTo(id - 1);
    }

    @Test
    public void findPrevByIdNotDeleted_이전_없는_테스트() {
        //given
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            News news = News.builder()
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            em.persist(news);
            newsList.add(news);
        }
        em.flush();
        em.clear();
        newsList.sort(Comparator.comparing(News::getTitle));
        Long id = newsList.get(0).getId();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<News> prevNews = newsRepository.findPrevByIdNotDeleted(id, pageRequest);

        //then
        assertThat(prevNews.size()).isZero();
    }

    @Test
    public void findNextByIdNotDeleted_다음_존재_테스트() {
        //given
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            News news = News.builder()
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            em.persist(news);
            newsList.add(news);
        }
        em.flush();
        em.clear();
        newsList.sort(Comparator.comparing(News::getTitle));
        Long id = newsList.get(3).getId();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<News> nextNews = newsRepository.findNextByIdNotDeleted(id, pageRequest);

        //then
        assertThat(nextNews.size()).isEqualTo(1);
        assertThat(nextNews.get(0).getId()).isEqualTo(id + 1);
    }

    @Test
    public void findNextByIdNotDeleted_다음_없는_테스트() {
        //given
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            News news = News.builder()
                    .publishTime(LocalDateTime.now())
                    .writer("작성자")
                    .title("제목" + i)
                    .content("내용" + i)
                    .build();
            em.persist(news);
            newsList.add(news);
        }
        em.flush();
        em.clear();
        newsList.sort(Comparator.comparing(News::getTitle));
        Long id = newsList.get(9).getId();

        ///when
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<News> nextNews = newsRepository.findNextByIdNotDeleted(id, pageRequest);

        //then
        assertThat(nextNews.size()).isZero();
    }

    @Test
    public void findByNewsCodeNotDeleted_존재_테스트() {
        String code = "123a!";
        News news = News.builder()
                .publishTime(LocalDateTime.now())
                .writer("작성자")
                .title("제목")
                .content("내용")
                .newsCode(code)
                .build();
        em.persist(news);
        em.flush();
        em.clear();

        ///when
        Optional<News> findNews = newsRepository.findByNewsCodeNotDeleted(code);

        //then
        assertThat(findNews).isNotEmpty();
        assertThat(findNews.get().getId()).isEqualTo(news.getId());
    }

    @Test
    public void findByNewsCodeNotDeleted_존재_안함_테스트() {
        //given
        String code = "123a!";

        ///when
        Optional<News> findNews = newsRepository.findByNewsCodeNotDeleted(code);

        //then
        assertThat(findNews).isEmpty();
    }

}