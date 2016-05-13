$(function() {
  $.material.init();

  $(window).resize(function() {
    $('#content > .container').css('padding-bottom', $('footer').height() + 40);
  });

  $(window).trigger('resize');
});
