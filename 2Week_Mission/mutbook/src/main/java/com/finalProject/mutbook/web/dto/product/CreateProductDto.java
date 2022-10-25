package com.finalProject.mutbook.web.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateProductDto {

    private String subject;

    private int price;

    private Long postKeywordId;
}
