package com.ggc.study_scala.utilities.event_bus.simple_usage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件实体
 */
@Getter
@AllArgsConstructor
public class Event {
    private final String type;
    private final String message;

}
