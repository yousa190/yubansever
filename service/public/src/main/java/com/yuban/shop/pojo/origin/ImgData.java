package com.yuban.shop.pojo.origin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImgData {
    private String url="http://localhost:8080/images/";
    private String alt;
    private String href;

    public ImgData(String url, String alt, String href) {
        this.url += url;
        this.alt = alt;
        this.href = href;
    }


}
