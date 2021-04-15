package net.beeapm.ui.model.result.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yuanlong.chen
 * @date 2021/04/15
 */
@Getter
@Setter
@ToString
@Builder
public class NameValue {
    private String name;
    private Object value;
}
