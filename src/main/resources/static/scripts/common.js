var MONTH_NAME_FORMAT = 'MMMM';

function getMonths() {
    var months = [];
    for (var i = 0; i < 12; i++) {
        months.push(moment().month(i).format(MONTH_NAME_FORMAT));
    }
    return months;
}

function getCurrentMonth() {
    return new Date().getMonth() + 1;
}

function getShortMonths() {
    return moment().getShortMonths();
}

function getZeroInsteadOfUndefined(value) {
    return value === undefined ? 0 : value;
}

function formatViewDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [day, month, year].join('-');
}