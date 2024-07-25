package com.zequence.ZequenceIms.enums;


import lombok.*;


@AllArgsConstructor
@Getter
@RequiredArgsConstructor
public enum ProductAndStockStatus {
        OUTOFSTOCK("OUT-OF-STOCK"),
        INSTOCK("IN-STOCK"),
        RUNNINGOUT("RUNNING-OUT-OF-STOCK");

        private String availability;
}
