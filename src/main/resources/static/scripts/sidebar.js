function sidebarChange() {
    if ($('body').hasClass('sidebar-mini')) {
        $('body').removeClass('sidebar-mini');
    } else {
        $('body').addClass('sidebar-mini');
    }
}