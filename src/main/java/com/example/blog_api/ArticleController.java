package com.example.blog_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    // GET /articles → liste tous les articles
    @GetMapping
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // GET /articles/{id} → récupère un article par id
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // POST /articles → crée un nouvel article
    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    // PUT /articles/{id} → met à jour un article
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article updatedArticle) {
        return articleRepository.findById(id).map(article -> {
            article.setTitle(updatedArticle.getTitle());
            article.setContent(updatedArticle.getContent());
            return ResponseEntity.ok(articleRepository.save(article));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /articles/{id} → supprime un article
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            articleRepository.delete(article.get());
            return ResponseEntity.noContent().build(); // ResponseEntity<Void>
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
