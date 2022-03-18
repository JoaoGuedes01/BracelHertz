window.onload = async function () {


  get_instituicoes()

  const botaoRegistar = document.getElementById("botaoRegistar");
  botaoRegistar.addEventListener("click", registar);


  async function registar() {
    event.preventDefault();
    var data = {};




    data.name = document.getElementById("Fname").value.trim();
    data.email = document.getElementById("email").value.trim();
    data.birthDate = document.getElementById("dataNascimento").value.trim();
    data.username = document.getElementById("username").value.trim();
    data.contact = document.getElementById("contact").value.trim();
    data.photo = "";
    data.nationality = document.getElementById("nacionalidade").value.trim();
    data.location = document.getElementById("localidade").value.trim();
    data.address = document.getElementById("morada").value.trim();
    data.roles = [{ id: parseInt(document.getElementById("instt").value.trim()) }];
    data.password = document.getElementById("password").value.trim();
    data.prison = { prisonId: document.getElementById("local").value };
    data.passwordToken = "";



    if (Fname.value == "" || email.value == "" || dataNascimento.value == "" ||
      username.value == "" || contact.value == "" || nacionalidade.value == "" ||
      localidade.value == "" || morada.value == "" || password.value == "" || contact.value.length != 9) {
      Swal.fire(
        'Preencha todos os campos!',
        '',
        'warning'
      )
    } else {

      var verificar = document.getElementById("username").value.trim();

      const response = await fetch('http://127.0.0.1:8080/api/users/username-exists/' + verificar, {
        headers: {
          'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
      });
      const existe = await response.json();


      if (existe) {
        Swal.fire(
          'Este username já existe!',
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
          if (parseInt(size) >= 1000) {
            Swal.fire(
              'Ocorreu um erro!',
              'Foto apenas pode ter até 1 MB inclusive',
              'warning'
            )
          } else {


            if (validacaoEmail(document.getElementById("email"))) {
              if (valida_nome(Fname.value) || valida_nome(document.getElementById("nacionalidade"))
                || valida_nome(document.getElementById("localidade"))) {
                if (valida()) {
                  Swal.fire(
                    'Palavra-passe não cumpre os requisitos!',
                    '',
                    'warning'
                  )
                } else {

                  await fetch('http://127.0.0.1:8080/api/users', {
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
                  'Nome, Localidade e Nacionalidade apenas podem ter letras!',
                  '',
                  'warning'
                )
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



  }





  async function get_instituicoes() {


    const response = await fetch('http://127.0.0.1:8080/api/prisons', {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'GET',
      credentials: 'include'
    });

    const response1 = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'GET',
      credentials: 'include'
    });
    const instituicoes = await response.json();
    const logado = await response1.json();
    var show_inst = "";

    let RoleLogado = localStorage.getItem("RoleLogado");

    if (RoleLogado == "ROLE_MANAGER") {
      show_inst += "<option value='" + logado.prison.prisonId + "'>" + logado.prison.name + "</option>";
      document.getElementById("instt").remove(1);
      document.getElementById("instt").remove(1);
    } else {
      for (var inst of instituicoes) {
        show_inst += "<option value='" + inst.prisonId + "'>" + inst.name + "</option>";
      }
    }

    document.getElementById("local").innerHTML = show_inst;

  }



};

//-------------------------------------------------------------------------------------------------------
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

function valida_nome(elemento) {
  var filter_nome = /^([a-zA-Zà-úÀ-Ú]|\s+)+$/;
  if (!filter_nome.test(elemento)) {
    return false;
  } else {
    return true;
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
//----------Só aceita letras e um espaço e pontos, virgulas----------------
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


function valida() {
  if (document.getElementById("password").validity.patternMismatch) {

    return true;
  } else {

    return false;
  }

}


var myInput420 = document.getElementById("password");
var letter420 = document.getElementById("letter420");
var capital420 = document.getElementById("capital420");
var number420 = document.getElementById("number420");
var length420 = document.getElementById("length420");
var lengt420 = document.getElementById("lengt420");


myInput420.onfocus = function () {
  document.getElementById("message").style.display = "block";
}


myInput420.onblur = function () {
  document.getElementById("message").style.display = "none";
}

myInput420.onkeyup = function () {

  var lowerCaseLetters = /[a-z]/g;
  if (myInput420.value.match(lowerCaseLetters)) {
    letter420.classList.remove("invalid420");
    letter420.classList.add("valid420");
  } else {
    letter420.classList.remove("valid420");
    letter420.classList.add("invalid420");
  }

  var upperCaseLetters = /[A-Z]/g;
  if (myInput420.value.match(upperCaseLetters)) {
    capital420.classList.remove("invalid420");
    capital420.classList.add("valid420");
  } else {
    capital420.classList.remove("valid420");
    capital420.classList.add("invalid420");
  }

  var numbers = /[0-9]/g;
  if (myInput420.value.match(numbers)) {
    number420.classList.remove("invalid420");
    number420.classList.add("valid420");
  } else {
    number420.classList.remove("valid420");
    number420.classList.add("invalid420");
  }

  if (myInput420.value.length >= 6) {
    length420.classList.remove("invalid420");
    length420.classList.add("valid420");
    lengt420.classList.remove("invalid420");
    lengt420.classList.add("valid420");
  } else {
    length420.classList.remove("valid420");
    length420.classList.add("invalid420");
    lengt420.classList.remove("valid420");
    lengt420.classList.add("invalid420");
  }

  if (myInput420.value.length <= 24 && myInput420.value.length >= 6) {
    lengt420.classList.remove("invalid420");
    lengt420.classList.add("valid420");
  } else {
    lengt420.classList.remove("valid420");
    lengt420.classList.add("invalid420");
  }
}

//---------------------------------------------------------------------------------------------------

var continf = document.getElementById("contact");
var nomeinf = document.getElementById("Fname");
var mailinf = document.getElementById("email");
var nacinf = document.getElementById("nacionalidade")
var locinf = document.getElementById("localidade");
var existeUser = document.getElementById("username");

continf.onkeyup = function () {
  if (document.getElementById("contact").value.length == 9 || document.getElementById("contact").value.length == 0) {
    document.getElementById("continf").style.display = "none";
  } else {
    document.getElementById("continf").style.display = "block"
  }
}

nomeinf.onkeyup = function () {
  if (valida_nome(document.getElementById("Fname").value) || document.getElementById("Fname").value == "") {
    document.getElementById("nomeinf").style.display = "none";
  } else {
    document.getElementById("nomeinf").style.display = "block"
  }
}

mailinf.onkeyup = function () {
  if (validacaoEmail(document.getElementById("email")) || document.getElementById("email").value == "") {
    document.getElementById("mailinf").style.display = "none";
  } else {
    document.getElementById("mailinf").style.display = "block"
  }
}

nacinf.onkeyup = function () {
  if (valida_nome(document.getElementById("nacionalidade").value) || document.getElementById("nacionalidade").value == "") {
    document.getElementById("nacinf").style.display = "none";
  } else {
    document.getElementById("nacinf").style.display = "block"
  }
}

locinf.onkeyup = function () {
  if (valida_nome(document.getElementById("localidade").value) || document.getElementById("localidade").value == "") {
    document.getElementById("locinf").style.display = "none";
  } else {
    document.getElementById("locinf").style.display = "block"
  }
}

existeUser.oninput = async function UsernameTaken() {

  var verificar = document.getElementById("username").value.trim();

  const response = await fetch('http://127.0.0.1:8080/api/users/username-exists/' + verificar, {
    headers: {
      'Content-Type': 'application/json'
    },
    mode: 'cors',
    method: 'GET',
    credentials: 'include'
  });
  const existe = await response.json();


  if (existe && verificar !== "") {
    document.getElementById("existeUser").style.display = "block";
  } else {
    document.getElementById("existeUser").style.display = "none";
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

async function post_photo(photoC, idGajo) {


  fetch('http://127.0.0.1:8080/api/users/upload-photos/' + idGajo, {
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
          'Utilizador registado com sucesso!',
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

