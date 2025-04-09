package com.hsbc.hap.cer.service;

import com.hsbc.hap.cer.model.QueryResult;
import java.util.List;

public interface HapCERService {
    List<QueryResult> executeQueries(List<String> queries);
}
