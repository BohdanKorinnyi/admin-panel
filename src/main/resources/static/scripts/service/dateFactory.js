"use strict";

Application.factory("dateFactory", function () {

    var factory ={};
    var today = new Date();

    factory.pickDateFrom = function (date) {
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
                var thisMonth = new Date(today.getFullYear(), today.getMonth(), 1);
                return thisMonth;
                break;
            case 'lastMonth':
                var lastMonth = new Date(today.getFullYear(), today.getMonth()-1, 1);
                return lastMonth;
                break;
            default:
                var todayDate = new Date();
                return todayDate;
    }};

    factory.pickDateTo = function (date) {
        switch (date) {
            case 'today':
                var todayDate = new Date();
                return todayDate;
                break;
            case 'yesterday':
                var todayDate = new Date();
                return todayDate;
                break;
            case 'lastWeek':
                var todayDate = new Date();
                return todayDate;
                break;
            case 'thisMonth':
                var todayDate = new Date();
                return todayDate;
                break;
            case 'lastMonth':
                var lastMonth = new Date(today.getFullYear(), today.getMonth(), 0);
                return lastMonth;
                break;
            default:
                var todayDate = new Date();
                return todayDate;
        }};

    return factory;
});