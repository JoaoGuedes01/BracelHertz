window.onload = async function () {

  var data = {};

  const botaoRegistar = document.getElementById("botaoRegistar");
  botaoRegistar.addEventListener("click", registar);

  async function registar() {

    data.name = document.getElementById("name").value.trim();
    data.email = document.getElementById("email").value.trim();
    data.contact = document.getElementById("contact").value.trim();
    data.description = document.getElementById("descricao").value.trim();
    data.location = document.getElementById("localidade").value.trim();
    data.address = document.getElementById("morada").value.trim();


    if (document.getElementById("name").value.trim() == "" || document.getElementById("email").value.trim() == "" ||
      document.getElementById("contact").value.trim() == "" || document.getElementById("descricao").value.trim() == "" ||
      document.getElementById("localidade").value.trim() == "" || document.getElementById("morada").value.trim() == "" ||
      document.getElementById("contact").value.length !== 9) {

      Swal.fire(
        'Preencha todos os campos!',
        '',
        'warning'
      )

    } else {
      if (pic == "") {
        Swal.fire(
          'É obrigatória uma fotografia!',
          '',
          'warning'
        )
      } else {

        if (validacaoEmail(document.getElementById("email"))) {

          if (parseInt(size) >= 1000) {
            Swal.fire(
              'Ocorreu um erro!',
              'Foto apenas pode ter até 1 MB inclusive',
              'warning'
            )
          } else {


            await fetch('http://127.0.0.1:8080/api/prisons', {
              headers: {
                'Content-Type': 'application/json'
              },
              mode: 'cors',
              method: 'POST',
              credentials: 'include',
              body: JSON.stringify(data)

            }).then(function (response) {
              if (!response.ok) {
                alert(response);
                throw new Error("ERRO");
              }
              console.log(response);
              return response.json();
            }).then(async function (result) {
              console.log(result);
              if (result) {


                post_photo(formData, result.objectId);


              }




            }).catch(function (err) {
              swal("Erro!", "Erro!", "error");
            })



          }

        } else {
          Swal.fire(
            'Este email não é válido!',
            '',
            'warning'
          )
        }


      }
    }






  }



}


//--------------------------------------Acept image---------------------------------------------------
let userLogado = localStorage.getItem("userLogado");
var pic = "";
var formData = "";
var loadFile = function (event) {

  formData = new FormData();
  formData.append("file", event.target.files[0]);
  size = ~~(event.target.files[0].size / 1024);
  pic = "a";

};

async function post_photo(photoC, idPrisao) {


  fetch('http://127.0.0.1:8080/api/prisons/upload-photos/' + idPrisao, {
    mode: 'cors',
    method: 'PUT',
    body: photoC,
    credentials: 'include'
  })
    .then(function (response) {
      //console.log(response.headers.get('Set-Cookie'));
      console.log(response);
      if (!response.ok) {
        throw new Error(response.statusText);
      }
      return response.json();
    })
    .catch(function (err) {
      //swal.showValidationError('Pedido falhado: ' + err);
      console.log(err); // estava alert(err); coloquei console log para não estar sempre a aparecer pop-up ao utilizador
    })
    .then(async function (result) {
      console.log(result);
      if (result) {



        Swal.fire(
          'Instituição registada com sucesso!',
          '',
          'success'
        ).then(() => {
          location.reload();
        })



      }
      else {
        swal("Erro!", "Erro!", "error");
        console.log(result);
        //swal({ title: `${result.value.userMessage.message.pt}` });
      }
    });


}


//--------------------------------------------------------------------------------------------------------
var slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
  showSlides(slideIndex += n);
}

function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  var shi;
  var slides = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("dot");
  if (n > slides.length) { slideIndex = 1 }
  if (n < 1) { slideIndex = slides.length }
  for (shi = 0; shi < slides.length; shi++) {
    slides[shi].style.display = "none";
  }
  for (shi = 0; shi < dots.length; shi++) {
    dots[shi].className = dots[shi].className.replace(" active", "");
  }
  slides[slideIndex - 1].style.display = "block";
  dots[slideIndex - 1].className += " active";
}

//--------------------------------------------------------------------------------------------------------
function validacaoEmail(field) {
  usuario = field.value.substring(0, field.value.indexOf("@"));
  dominio = field.value.substring(field.value.indexOf("@") + 1, field.value.length);

  if ((usuario.length >= 1) &&
    (dominio.length >= 3) &&
    (usuario.search("@") == -1) &&
    (dominio.search("@") == -1) &&
    (usuario.search(" ") == -1) &&
    (dominio.search(" ") == -1) &&
    (dominio.search(".") != -1) &&
    (dominio.indexOf(".") >= 1) &&
    (dominio.lastIndexOf(".") < dominio.length - 1)) {

    return true;
  }
  else {

    return false;
  }
}



//----------Só um espaço----------------
$('.space1').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú']/g, ' '));
  $th.val($th.val().replace(/^\s*/, ''));
})
//----------Sem espaços e numeros----------------
$('.nspace1').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú']/g, ' '));
  $th.val($th.val().replace(/[' ']/g, ''));
})
//----------Sem espaços com numeros----------------
$('.nspace').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú\d']/g, ' '));
  $th.val($th.val().replace(/[' ']/g, ''));
})
//----------Só numeros----------------
$('.snum').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^\d']/g, ' '));
  $th.val($th.val().replace(/[' ']/g, ''));
})
//----------Só aceita letras e um espaço e pontos, virgulas----- regex-----------
$('.1spaceand').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú\d.,!?()$€ªº:@_\-']/g, ' '));
  $th.val($th.val().replace(/^\s*/, ''));
})
//--------------------------Morada--regex--------------------
$('.regMorada').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú\d.,ªº\-']/g, ' '));
  $th.val($th.val().replace(/^\s*/, ''));
})

//-------------------------------------------------------------------------------------------

var mailinf = document.getElementById("email");
var continf = document.getElementById("contact");

mailinf.onkeyup = function () {
  if (validacaoEmail(document.getElementById("email")) || document.getElementById("email").value == "") {
    document.getElementById("mailinf").style.display = "none";
  } else {
    document.getElementById("mailinf").style.display = "block"
  }
}


continf.onkeyup = function () {
  if (document.getElementById("contact").value.length == 9 || document.getElementById("contact").value.length == 0) {
    document.getElementById("continf").style.display = "none";
  } else {
    document.getElementById("continf").style.display = "block"
  }
}