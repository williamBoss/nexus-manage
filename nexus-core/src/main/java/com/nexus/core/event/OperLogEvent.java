package com.nexus.core.event;

import com.nexus.core.domain.entity.SysOperLog;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperLogEvent {
	private SysOperLog operLog;
}
