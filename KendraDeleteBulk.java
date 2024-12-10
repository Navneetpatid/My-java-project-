package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

import java.util.List;

@Data
public class KendraDeleteBulk {
    List<Long> kendraIds;
}
