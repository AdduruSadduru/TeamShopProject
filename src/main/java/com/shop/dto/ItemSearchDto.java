package com.shop.dto;

import com.shop.constant.Dessert;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; // 조회날짜

    private ItemSellStatus searchSellStatus; // 상태

    private String searchBy; // 조회 유형

    private String searchQuery = ""; // 검색 단어

    private Dessert searchDessertType;


    private List<Board> boardList;
}
