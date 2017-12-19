function sidebarChange() {
    if ($('body').hasClass('sidebar-mini')) {
        $('#mini-logo').css('display', 'none');
        $('#normal-logo').addClass('text-center');
        $('body').removeClass('sidebar-mini');
    } else {
        $('#mini-logo').css('display', '');
        $('#normal-logo').removeClass('text-center');
        $('body').addClass('sidebar-mini');
    }
}