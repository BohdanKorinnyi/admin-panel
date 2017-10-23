package com.omnia.admin.dao;

import com.omnia.admin.model.Conversion;

import java.util.Optional;

public interface ConversionDao {
    Optional<Conversion> findConversionByPrefixAndClickId(String prefix, String clickId);
}
