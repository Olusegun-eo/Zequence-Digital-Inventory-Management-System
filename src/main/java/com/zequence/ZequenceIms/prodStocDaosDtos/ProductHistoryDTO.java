package com.zequence.ZequenceIms.prodStocDaosDtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistoryDTO {
    private Long id;
    private String productDescription;
    private LocalDateTime changeTimestamp;
}
