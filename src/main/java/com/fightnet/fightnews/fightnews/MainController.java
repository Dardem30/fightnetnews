package com.fightnet.fightnews.fightnews;

import com.sendgrid.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class MainController {
    private final FightNewsService service;
    @GetMapping("getArticles")
    public ResponseEntity<SearchResult> getArticles(@RequestParam(required = false) final String page) {
        try {
            return ResponseEntity.ok(service.getArticles(page));
        } catch (Exception e) {
            sendErrorMessage("Error during trying to parse news");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SearchResult());
        }
    }
    @GetMapping("getContentArticle")
    public ResponseEntity<SearchResultArticle> getContentArticle(@RequestParam final String href) {
        try {
            return ResponseEntity.ok(service.getContentArticle(href));
        } catch (Exception e) {
            sendErrorMessage("Error during trying to get article content");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SearchResultArticle());
        }
    }
    @GetMapping("getRussianArticles")
    public ResponseEntity<SearchResult> getRussianArticles(@RequestParam(required = false) final String page) {
        try {
            return ResponseEntity.ok(service.getRussianArticles(page));
        } catch (Exception e) {
            sendErrorMessage("Error during trying to parse russian news");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SearchResult());
        }
    }
    @GetMapping("getArticleContentRussian")
    public ResponseEntity<SearchResultArticle> getArticleContentRussian(@RequestParam final String href) {
        try {
            return ResponseEntity.ok(service.getArticleContentRussian(href));
        } catch (Exception e) {
            sendErrorMessage("Error during trying to get article content russian");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SearchResultArticle());
        }
    }
    private void sendErrorMessage(final String subject) {
        try {
            final Request request = new Request();
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = new Mail(new Email("fightseeker0@gmail.com"),
                    "Error during trying to parse news",
                    new Email("r.lukashenko@mail.ru"), new Content("text/plain",
                    "error")).build();
            new SendGrid("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").api(request);
        } catch (IOException ignored) {}
    }
}
