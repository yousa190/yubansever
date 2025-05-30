package com.yuban.pojo.SpecGroup;

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