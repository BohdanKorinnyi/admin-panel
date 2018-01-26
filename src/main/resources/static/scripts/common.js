var MONTH_NAME_FORMAT = 'MMMM';

function getMonths() {
    var months = [];
    for (var i = 0; i < 12; i++) {
        months.push(moment().month(i).format(MONTH_NAME_FORMAT));
    }
    return months;
}

function getShortMonths() {
    return moment().getShortMonths();
}

function getZeroInsteadOfUndefined(value) {
    return value === undefined ? 0 : value;
}