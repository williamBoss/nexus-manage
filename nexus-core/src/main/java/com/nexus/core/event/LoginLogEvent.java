package com.nexus.core.event;

import com.nexus.core.domain.entity.SysLogininfor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginLogEvent {
	private SysLogininfor logininfor;
}