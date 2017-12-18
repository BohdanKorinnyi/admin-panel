package com.omnia.admin.service;

public interface SpentService {
    Float getSpentByCurrentMonth(Integer buyerId);

    Float getSpentByToday(Integer buyerId);

    Float getSpentByYesterday(Integer buyerId);
}
