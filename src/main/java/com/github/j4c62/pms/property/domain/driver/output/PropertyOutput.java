package com.github.j4c62.pms.property.domain.driver.output;

import com.github.j4c62.pms.property.domain.aggregate.vo.Id;
import com.github.j4c62.pms.property.domain.aggregate.vo.Status;
import java.util.List;


public record PropertyOutput(Id id, Status status, List<String> errors) {}
