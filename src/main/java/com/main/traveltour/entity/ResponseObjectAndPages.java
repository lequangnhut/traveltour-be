package com.main.traveltour.entity;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseObjectAndPages {
    private String status;
    private String message;
    private Object data;
    private Integer totalPages;
}