package com.omnia.admin.grid.filter.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class AdvertiserNameFilterTest {

    @InjectMocks
    private AdvertiserNameFilter advertiserNameConversionFilter;

    @Test
    public void getSql() throws Exception {
        assertThat(advertiserNameConversionFilter.getSql("test"), is("adverts.advshortname LIKE '%test%'"));
    }
}