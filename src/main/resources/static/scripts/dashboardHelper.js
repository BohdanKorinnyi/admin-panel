function tableRow(month, values) {
    var revenue = getDataByMonth(month, values[0].data);
    var spent = getDataByMonth(month, values[1].data);
    var paid = getDataArrayByMonth(month, values[2].data);
    var bonus = getDataByMonth(month, values[3].data);
    var paidTotal = calculatePaid(paid);
    return {
        month: month,
        revenue: revenue,
        spent: spent,
        bonus: bonus,
        paid: paid,
        paidTotal: paidTotal,
        profit: (revenue - spent).toFixed(2),
        liability: (bonus - paidTotal).toFixed(2)
    };
}

function calculatePaid(payments) {
    var result = 0;
    payments.map(function (value) {
        result += value.sum;
    });
    return result;
}

function getDataByMonth(month, data) {
    var monthValue = 0;
    data.map(function (value) {
        if (value.date === month) {
            monthValue = value.value;
        }
    });
    return monthValue;
}

function getDataArrayByMonth(month, data) {
    var monthValue = [];
    if (Array.isArray(data)) {
        data.map(function (value) {
            if (value.month === month) {
                monthValue.push(value);
            }
        });
    }
    return monthValue;
}