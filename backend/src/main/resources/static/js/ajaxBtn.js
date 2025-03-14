$(window).on("load", function () {

  valueIndex(1);

  //MY FRIENDS
  $("#btnMoreMyFriends").on("click", () =>
    functionMoreMyFriends(
      "#moreMyFriends",
      "#spinner",
      "#btnMoreMyFriends"
    )
  );

  //MY FRIENDS
  $("#btnMorePendingFriends").on("click", () =>
    functionMorePendingFriends(
      "#morePendingFriends",
      "#spinner",
      "#btnMorePendingFriends"
    )
  );


});


//MY FRIENDS
function functionMorePendingFriends(where, spinner, button) {
  var value = indexPendingFriends;
  this.indexPendingFriends += 1;

  var url = "/morePendingFriends?page=" + value;
  ajaxCall(url, spinner, where, button);
}

//MY FRIENDS
function functionMoreMyFriends(where, spinner, button) {
  var value = indexMyFriends;
  this.indexMyFriends += 1;

  var url = "/moreMyFriends?page=" + value;
  ajaxCall(url, spinner, where, button);
}



var indexPendingFriends;
var indexMyFriends;
var indexSubejctAdmin;


function ajaxCall(url, spinner, where, button) {
  $.ajax({
    type: "GET",
    contentType: "aplication/json",
    url: url,
    beforeSend: function () {
      $(spinner).removeClass("hidden");
    },
    success: function (result) {
      $(where).append(result);
    },
    error: function (e) {
      $(button).addClass("hidden");
    },
    complete: function () {
      $(spinner).addClass("hidden");
    },
  });
}

function valueIndex(num) {
  this.indexPendingFriends = num;
  this.indexMyFriends = num;
  this.indexSubejctAdmin = num;
}






//Subjects admin
function functionMoreExamsTeacher(where, spinner, button) {
  var value = indexSubejctAdmin;
  this.indexSubejctAdmin += 1;

  const arrayPath = window.location.pathname.split("/");
  const id = arrayPath[3];

  var url = "/teachers/subject/" + id + "/type-exams/moreTeacherExam?page=" + value;
  ajaxCall(url, spinner, where, button);
}



