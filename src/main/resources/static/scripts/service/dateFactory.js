/**
 * Created by meser on 10/9/2017.
 */
"use strict";

Application.factory("dateFactory", function () {

    var factory ={};
    var today = new Date();

    factory.pickDate = function (date) {
        switch (date) {
            case 'today':
                var todayDate = new Date();
                return todayDate;
                break;
            case 'yesterday':
                var previousDay = new Date(today);
                return previousDay.setDate(today.getDate() - 1);
                break;
            case 'lastWeek':
                var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 7);
                return lastWeek;
                break;
            case 'thisMonth':
                var lastMonth = new Date(today);
                return lastMonth.setMonth(today.getMonth() - 1);
                break;
            case 'lastMonth':
                var lastMonth = new Date(today);
                return lastMonth.setMonth(today.getMonth() - 2);
                break;
            default:
                var todayDate = new Date();
                return todayDate;
    }};

    return factory;
});