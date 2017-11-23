package com.omnia.admin.service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface DashboardService {
    Map<String, Object> getDashboardData(Integer buyerId) throws ExecutionException, InterruptedException;
}
