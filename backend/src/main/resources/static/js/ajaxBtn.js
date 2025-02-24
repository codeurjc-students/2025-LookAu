$(window).on("load", function () {

  valueIndex(1);

  //subjects admin
  $("#btnMoreAdminSubjects").on("click", () =>
    functionMoreSubejctAdmin(
      "#moreAdminSubjects",
      "#spinner",
      "#btnMoreAdminSubjects"
    )
  );


  //subjects main
  $("#btnMoreMainSubjects").on("click", () =>
    functionMoreSubejctAdmin(
      "#moreMainSubjects",
      "#spinner",
      "#btnMoreMainSubjects"
    )
  );


  //exam teacher
  $("#btnMoreTeacherExams").on("click", () =>
    functionMoreExamsTeacher(
      "#moreTeacherExams",
      "#spinner",
      "#btnMoreTeacherExams"
    )
  );

});



var indexAdminSubjects;
var indexMainSubjects;
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

//Subjects admin
function functionMoreSubejctAdmin(where, spinner, button) {
  var value = indexAdminSubjects;
  this.indexAdminSubjects += 1;

  var url = "/moreSubjectsAdmin?page=" + value;
  ajaxCall(url, spinner, where, button);
}


//Subjects admin
function functionMoreSubejctAdmin(where, spinner, button) {
  var value = indexMainSubjects;
  this.indexAdminSubjects += 1;

  var url = "/moreSubjectsMain?page=" + value;
  ajaxCall(url, spinner, where, button);
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


function valueIndex(num) {
  this.indexAdminSubjects = num;
  this.indexMainSubjects = num;
  this.indexSubejctAdmin = num;
}
