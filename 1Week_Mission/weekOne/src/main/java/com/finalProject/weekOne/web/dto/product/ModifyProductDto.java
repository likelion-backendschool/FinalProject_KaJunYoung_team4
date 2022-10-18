package com.finalProject.weekOne.web.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ModifyProductDto {

    private String subject;

    private int price;

    private Long postKeywordId;
}
