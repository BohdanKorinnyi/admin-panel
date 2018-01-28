Application.factory("transferService", function () {
    var value = null;

    var service = {
        setParam: setParam,
        getParam: getParam
    };

    return service;

    function setParam(parameter) {
        value = parameter;
    }

    function getParam() {
        return value;
    }
});