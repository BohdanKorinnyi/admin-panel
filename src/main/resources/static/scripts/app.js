var Application = angular.module("Application", [
    "ngRoute",
    "ngResource",
    "720kb.datepicker",
    "ui.bootstrap",
    "nya.bootstrap.select"
]);

Application.constant("routeForUnauthorizedAccess", "/");
Application.constant("roles", {
    BUYER: 1,
    TEAM_LEADER: 2,
    CBO: 3,//Media Buyer Director
    MENTOR:4,
    CFO: 5,//Chief Finance Office
    DIRECTOR: 6,
    ADMIN: 7
});