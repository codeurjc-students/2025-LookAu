$(window).on("load", function () {

  valueIndex(1);

  //MY FRIENDS
  $("#btnMoreMyFriends").on("click", () =>
    functionMoreMyFriends(
      "#moreMyFriends",
      "#spinner1",
      "#btnMoreMyFriends"
    )
  );

  //PENDING FRIENDS
  $("#btnMorePendingFriends").on("click", () =>
    functionMorePendingFriends(
      "#morePendingFriends",
      "#spinner2",
      "#btnMorePendingFriends"
    )
  );

  //REQUEST FRIENDS
  $("#btnMoreRequestFriends").on("click", () =>
    functionMoreRequestFriends(
      "#moreRequestFriends",
      "#spinner3",
      "#btnMoreRequestFriends"
    )
  );

  //DELETE FRIENDS
  $("#btnMoreDeleteFriends").on("click", () =>
    functionMoreDeleteFriends(
      "#moreDeleteFriends",
      "#spinner3",
      "#btnMoreDeleteFriends"
    )
  );

});


//DELETE FRIENDS
function functionMoreDeleteFriends(where, spinner, button) {
  var value = indexDeleteFriends;
  this.indexDeleteFriends += 1;

  var url = "/moreDeleteFriends?page=" + value;
  ajaxCall(url, spinner, where, button);
}

//REQUEST FRIENDS
function functionMoreRequestFriends(where, spinner, button) {
  var value = indexRequestFriends;
  this.indexRequestFriends += 1;

  var url = "/moreRequestFriends?page=" + value;
  ajaxCall(url, spinner, where, button);
}

//PENDING FRIENDS
function functionMorePendingFriends(where, spinner, button) {
  var value = indexPendingFriends;
  this.indexPendingFriends += 1;

  var url = "/morePendingFriends?page=" + value;
  ajaxCall(url, spinner, where, button);
}

//DELETE FRIENDS
function functionMoreMyFriends(where, spinner, button) {
  var value = indexMyFriends;
  this.indexMyFriends += 1;

  var url = "/moreMyFriends?page=" + value;
  ajaxCall(url, spinner, where, button);
}



var indexPendingFriends;
var indexMyFriends;
var indexSubejctAdmin;
var indexDeleteFriends;


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

      const isLastPage = $(where).find("[data-is-last-page]").last().attr("data-is-last-page") === "true";
      if (isLastPage) {
        $(button).addClass("hidden"); // Oculta el botón "More" si es la última página
      } else {
        $(button).removeClass("hidden"); // Muestra el botón "More" si hay más páginas
      }

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
  this.indexRequestFriends = num;
  this.indexDeleteFriends = num;
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



