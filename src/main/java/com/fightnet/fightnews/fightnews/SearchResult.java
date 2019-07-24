package com.fightnet.fightnews.fightnews;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private Integer count;
    private List<FightNews> records;
}
