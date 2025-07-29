package com.yuban.shop.pojo.origin.categoryConfig;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpecItem {
    private String title;
    private List<SpecSubItem> children;
}