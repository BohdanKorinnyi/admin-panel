'use strict';

Application.factory('dateFactory', function () {
    var factory = {};
    var today = new Date();
    factory.pickDateFrom = function (date) {
        switch (date) {
            case 'today':
                return new Date();
                break;
            case 'yesterday':
                var previousDay = new Date(today);
                return previousDay.setDate(today.getDate() - 1);
                break;
            case 'lastWeek':
                return new Date(today.getFullYear(), today.getMonth(), today.getDate() - 7);
                break;
            case 'thisMonth':
                return new Date(today.getFullYear(), today.getMonth(), 1);
                break;
            case 'lastMonth':
                return new Date(today.getFullYear(), today.getMonth() - 1, 1);
                break;
            default:
                return new Date();
        }
    };

    factory.pickDateTo = function (date) {
        switch (date) {
            case 'today':
                return new Date();
                break;
            case 'yesterday':
                var previousDay = new Date(today);
                return previousDay.setDate(today.getDate() - 1);
                break;
            case 'lastWeek':
                return new Date();
                break;
            case 'thisMonth':
                return new Date();
                break;
            case 'lastMonth':
                return new Date(today.getFullYear(), today.getMonth(), 0);
                break;
            default:
                return new Date();
        }
    };
    return factory;
});