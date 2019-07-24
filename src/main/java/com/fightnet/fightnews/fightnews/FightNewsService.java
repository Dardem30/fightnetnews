package com.fightnet.fightnews.fightnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FightNewsService {
    public SearchResult getArticles(final String page) throws Exception {
        final Document doc = Jsoup.connect("https://www.mmafighting.com/latest-news/archives" + (page == null ? "" : page)).get();
        final SearchResult result = new SearchResult();
        final List<FightNews> news = new ArrayList<>(33);
        for (final Element element: doc.select("div[class$=c-compact-river]")) {
            for (final Element entry : element.select("div[class$=c-entry-box--compact c-entry-box--compact--article]")) {
                final FightNews fightNews = new FightNews();
                fightNews.setHref(entry.select("a[href]").first().attr("href"));
                fightNews.setTitle(entry.select("h2").first().text());
                fightNews.setCreateTime(entry.select("time").first().attr("datetime"));
                fightNews.setImage(entry.select("img").get(1).attr("src"));
                news.add(fightNews);
            }
        }
        final String[] paginationText = doc.select("span[class=c-pagination__text]").first().text().split(" ");
        result.setCount(Integer.valueOf(paginationText[paginationText.length - 1]));
        result.setRecords(news);
        return result;
    }

    public SearchResultArticle getContentArticle(final String href) throws Exception {
        final SearchResultArticle article = new SearchResultArticle();
        final Element mainElement =  Jsoup.connect(href).get().select("main[id=content]").first();
        article.setContent(mainElement.select("div[class=c-entry-content]").html());
        article.setImage(mainElement.select("img").first().attr("src"));
        return article;
    }

    public SearchResult getRussianArticles(final String page) throws Exception {
        final Document doc = Jsoup.connect("https://ringside24.com/ru/mma/" + (page == null ? "" : "?page=" + page)).get();
        final SearchResult result = new SearchResult();
        final List<FightNews> newsList = new ArrayList<>(11);
        for (final Element element: doc.select("div[class$=post-list-wr]").select("div[class$=post-list]")) {
            final FightNews news = new FightNews();
            news.setTitle(element.select("div[class$=h3]").text());
            news.setHref("https://ringside24.com" + element.select("a[href]").first().attr("href"));
            news.setImage("https://ringside24.com" + element.select("img").first().attr("src"));
            final String[] infoHtml = element.select("div[class$=post-list__info]").html().split("<br>");
            news.setCreateTime(infoHtml[infoHtml.length - 1]);
            newsList.add(news);
        }
        final Elements pagination = doc.select("div[class$=pagination]").select("li");
        result.setCount(Integer.valueOf(pagination.get(pagination.size() - 2).text()));
        result.setRecords(newsList);
        return result;
    }
    public SearchResultArticle getArticleContentRussian(final String href) throws Exception {
        final SearchResultArticle article = new SearchResultArticle();
        final Element mainElement =  Jsoup.connect(href).get().select("div[id=js-main-container]").first();
        final StringBuilder content = new StringBuilder();
        for (Element paragraph: mainElement.select("p")) {
            content.append(paragraph.text());
        }
        article.setContent(content.toString());
        article.setImage("https://ringside24.com" + mainElement.select("img").first().attr("src"));
        return article;
    }
}
