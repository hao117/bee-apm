package net.beeapm.ui.model.result.dashboard;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yuanlong.chen
 * @date 2021/04/13
 */
@Getter
@Setter
public class SummaryResult {
    private String title;
    private String icon;
    private Long value;
    private String color;
    private String action;
}
