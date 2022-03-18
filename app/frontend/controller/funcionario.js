$(window).on("load", function () {
  let RoleLogado = localStorage.getItem("RoleLogado");
  display_perfil();
  get_instituicoes();


  async function display_perfil() {

    let id_user_clicked = localStorage.getItem("id_user_clicked");
    const response = await fetch('http://127.0.0.1:8080/api/users/' + id_user_clicked, {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'GET',
      credentials: 'include'
    });
    const perfil = await response.json();



    const response7 = await fetch('http://127.0.0.1:8080/api/photos/' + perfil.photoId, {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'GET',
      credentials: 'include'
    });
    const photoD = await response7.json();


    var role = "";
    if (perfil.roles[0].id == 0) {
      role = "Guarda Prisional";
    } else if (perfil.roles[0].id == 1) {
      role = "Gestor de Instituição";
    } else if (perfil.roles[0].id == 2) {
      role = "Gestor da Rede Prisonal";
    }


    //envia a para a pagina
    document.getElementById("funcaoF").innerHTML = role;
    document.getElementById("nFuncionario").innerHTML = perfil.username;
    document.getElementById("id_instituicao").value = perfil.prison.prisonId;
    document.getElementById("dn_funcionario").value = perfil.birthDate;
    document.getElementById("nacF").value = perfil.nationality;
    document.getElementById("moradaF").value = perfil.address;
    document.getElementById("localF").value = perfil.location;
    document.getElementById("nomeF").value = perfil.name;
    document.getElementById("imagemPerfil").src = "data:image/png;base64," + photoD.picByte;
    document.getElementById("contactoF").value = perfil.contact;
    document.getElementById("emailF").value = perfil.email;

    if (perfil.lastLogin !== null) {
      document.getElementById("last_login").innerHTML = "Último login: " + getDate7(perfil.lastLogin);
    } else {
      document.getElementById("last_login").innerHTML = "Último login: ";
    }

    if (perfil.roles[0].name == "ROLE_NETWORKMAN") {
      document.getElementById("paraNETMAN").style.display = "none";
    }



    const response5 = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'GET',
      credentials: 'include'
    });
    const logado = await response5.json();





    /*
        if (!(RoleLogado == "ROLE_NETWORKMAN" && perfil.roles[0].name !== "ROLE_NETWORKMAN" || RoleLogado == "ROLE_MANAGER" && perfil.roles[0].name == "ROLE_GUARD")
          || logado.prison.prisonId !== perfil.prison.prisonId && RoleLogado !== "ROLE_NETWORKMAN") {
          document.getElementById("perfil_alterar_2").style.display = "none";
          document.getElementById("altPass").style.display = "none";
          document.getElementById("naoAnotar").style.display = "none";
        }*/

    if (RoleLogado == "ROLE_NETWORKMAN" && perfil.roles[0].name !== "ROLE_NETWORKMAN" ||
      RoleLogado == "ROLE_MANAGER" && perfil.roles[0].name == "ROLE_GUARD" && logado.prison.prisonId == perfil.prison.prisonId
      || logado.prison.prisonId == perfil.prison.prisonId && RoleLogado !== "ROLE_NETWORKMAN" && perfil.roles[0].name !== "ROLE_NETWORKMAN") {
      document.getElementById("perfil_alterar_2").style.display = "inline";
      document.getElementById("altPass").style.display = "inline";
      document.getElementById("naoAnotar").style.display = "block";
      document.getElementById("podeMudar").style.display = "block";

      document.getElementById("TabLogs").style.display = "block";
      getLogs();
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
    const instituicoes = await response.json();
    var show_inst = "";

    for (var inst of instituicoes) {
      show_inst += "<option value='" + inst.prisonId + "'>" + inst.name + "</option>";
    }

    document.getElementById("id_instituicao").innerHTML = show_inst;

  }


})

async function getLogs() {
  const response = await fetch('http://127.0.0.1:8080/api/user-logs/' + id_user_clicked, {
    headers: {
      'Content-Type': 'application/json'
    },
    mode: 'cors',
    method: 'GET',
    credentials: 'include'
  });
  const logsFun = await response.json();
  var see_logs = "";
  var tableLog = document.getElementById("LogsFun");

  for (const logFun of logsFun) {
    see_logs += "<td>" + getnDate(logFun.logTimestamp) + "</td>";

    see_logs += "<td>" + logFun.description + "</td>";

    if (logFun.byUser !== null) {
      see_logs += "<td id='" + logFun.byUser.userId + "' type='button' onclick='dothis(this.id)'>" + logFun.byUser.name + " (" + logFun.byUser.username + ")</td>";
    } else {
      see_logs += "<td>Utilizador apagado</td>";
    }

    see_logs += "</tr>";
  }
  tableLog.innerHTML = see_logs;

  var t = setTimeout(getLogs, 10000);
}


//-------------------------------------------------------------------------------------------------------

let RoleLogado = localStorage.getItem("RoleLogado");
let id_user_clicked = localStorage.getItem("id_user_clicked");



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

//----------Só aceita letras e um espaço----------------
$('.space1').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú']/g, ' '));
  $th.val($th.val().replace(/^\s*/, ''));
})
//----------Não aceita espaços nem aceita números----------------
$('.nspace1').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú']/g, ' '));
  $th.val($th.val().replace(/[' ']/g, ''));
})
//----------Não aceita espaços mas aceita números----------------
$('.nspace').keyup(function () {
  var $th = $(this);
  $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú\d']/g, ' '));
  $th.val($th.val().replace(/[' ']/g, ''));
})
//----------Só aceita numeros----------------
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





//--------------------------------------------EDITAR FUNCIONARIO-------------------------------------------

document.getElementById("perfil_save_2").addEventListener("click", async function () {

  var data = {};

  data.userId = id_user_clicked;
  data.birthDate = document.getElementById("dn_funcionario").value.trim();
  data.nationality = document.getElementById("nacF").value.trim();
  data.address = document.getElementById("moradaF").value.trim();
  data.location = document.getElementById("localF").value.trim();
  data.name = document.getElementById("nomeF").value.trim();
  data.contact = document.getElementById("contactoF").value.trim();
  data.email = document.getElementById("emailF").value.trim();
  data.prisonId = document.getElementById("id_instituicao").value;


  if (data.birthDate == "" || data.nationality == "" || data.address == "" || data.location == "" || data.name == "" ||
    data.contact == "" || data.contact.length !== 9 || data.email == "") {

    Swal.fire(
      'Preencha todos os campos!',
      '',
      'warning'
    )

  } else {
    if (validacaoEmail(document.getElementById("emailF"))) {



      fetch('http://127.0.0.1:8080/api/users/managers', {
        headers: {
          'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'PUT',
        body: JSON.stringify(data),
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


            const Toast = Swal.mixin({
              toast: true,
              position: 'top-end',
              showConfirmButton: false,
              timer: 1000,
              timerProgressBar: true,
              onOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
              }
            })

            Toast.fire({
              icon: 'success',
              title: 'Dados alterados com sucesso'
            }).then(() => {
              Myfunction425()
            })


          }
          else {
            Swal.fire(
              'Ocorreu um erro!',
              '',
              'error'
            ).then(() => {
              location.reload();
            })
            console.log(result);
            //swal({ title: `${result.value.userMessage.message.pt}` });
          }
        });

    } else {
      Swal.fire(
        'Este email não é válido!',
        '',
        'warning'
      )
    }

  }



})




//---------------------------------------------DATAS-------------------------------------------------

function getDate7(date) {
  var today = new Date(date);
  var d = today.getDate();
  var mo = today.getMonth();
  var a = today.getFullYear();
  var h = today.getHours();
  var m = today.getMinutes();
  h = checkTime(h);
  m = checkTime(m);
  d = checkTime(d);
  mo = checkTime(mo + 1);
  return d + "/" + mo + "/" + a + " " + h + ":" + m;
}
function checkTime(i) {
  if (i < 10) { i = "0" + i };  // add zero in front of numbers < 10
  return i;
}
function getnDate(date) {
  var today = new Date(date);
  var d = today.getDate();
  var mo = today.getMonth();
  var a = today.getFullYear();
  var h = today.getHours();
  var m = today.getMinutes();
  h = checkTime(h);
  m = checkTime(m);
  d = checkTime(d);
  mo = checkTime(mo + 1);
  return d + "/" + mo + "/" + a + " " + h + ":" + m;
}
//-----------------------------------------------------------------------------------------------------

document.getElementById("perfil_alterar_2").addEventListener("click", function () {

  if (RoleLogado == "ROLE_NETWORKMAN") {
    document.getElementById("id_instituicao").disabled = false;
    document.getElementById("icon_id_instituicao").style.display = "block";
  }
  Myfunction424();

})

function Myfunction424() {
  document.getElementById("perfil_alterar_2").style.display = "none";
  document.getElementById("perfil_save_2").style.display = "block";

  document.getElementById("nomeF").readOnly = false;
  document.getElementById("icon_nomeF").style.display = "block";
  document.getElementById("dn_funcionario").readOnly = false;
  document.getElementById("emailF").readOnly = false;
  document.getElementById("icon_emailF").style.display = "block";
  document.getElementById("contactoF").readOnly = false;
  document.getElementById("icon_contactoF").style.display = "block";
  document.getElementById("nacF").readOnly = false;
  document.getElementById("icon_nacF").style.display = "block";
  document.getElementById("moradaF").readOnly = false;
  document.getElementById("icon_moradaF").style.display = "block";
  document.getElementById("localF").readOnly = false;
  document.getElementById("icon_localF").style.display = "block";
}

function Myfunction425() {
  document.getElementById("perfil_save_2").style.display = "none";
  document.getElementById("perfil_alterar_2").style.display = "block";

  document.getElementById("id_instituicao").disabled = true;
  document.getElementById("icon_id_instituicao").style.display = "none";
  document.getElementById("nomeF").readOnly = true;
  document.getElementById("icon_nomeF").style.display = "none";
  document.getElementById("dn_funcionario").readOnly = true;
  document.getElementById("emailF").readOnly = true;
  document.getElementById("icon_emailF").style.display = "none";
  document.getElementById("contactoF").readOnly = true;
  document.getElementById("icon_contactoF").style.display = "none";
  document.getElementById("nacF").readOnly = true;
  document.getElementById("icon_nacF").style.display = "none";
  document.getElementById("moradaF").readOnly = true;
  document.getElementById("icon_moradaF").style.display = "none";
  document.getElementById("localF").readOnly = true;
  document.getElementById("icon_localF").style.display = "none";
}




//------------------------------------------------ALTERAR PASSWORD---------------------------------------------------------------

document.getElementById("editPass").addEventListener("click", function () {
  let id_user_clicked = localStorage.getItem("id_user_clicked");
  var nPassword = document.getElementById("nPassword");
  var data = {};

  if (nPassword.value.trim() == "") {
    Swal.fire(
      'Preencha o campo!',
      '',
      'warning'
    )
  } else {

    data.userId = id_user_clicked;
    data.newPassword = nPassword.value.trim();


    fetch('http://127.0.0.1:8080/api/users/passwords', {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'PUT',
      body: JSON.stringify(data),
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
            'Password alterada com sucesso!',
            '',
            'success'
          ).then(() => {
            location.reload();
          })


        }
        else {
          Swal.fire(
            'Ocorreu um erro!',
            '',
            'error'
          )
          console.log(result);
          //swal({ title: `${result.value.userMessage.message.pt}` });
        }
      });




  }

})



//------------------------------------------UPLOAD PHOTO------------------------------------------------------

var loadFile = function (event) {
  var image = document.getElementById('imagemPerfil');
  image.src = URL.createObjectURL(event.target.files[0]);

  const formData = new FormData();
  formData.append("file", event.target.files[0]);
  size = ~~(event.target.files[0].size / 1024);

  editar_photo(formData);


};


//------------------------------------------------PUT da foto----------------------------------------------------
async function editar_photo(photoC) {

  if (parseInt(size) >= 1000) {
    Swal.fire(
      'Ocorreu um erro!',
      'Foto apenas pode ter até 1 MB inclusive',
      'warning'
    )
      .then(() => {
        location.reload();
      })
  } else {


    fetch('http://127.0.0.1:8080/api/users/upload-photos/' + id_user_clicked, {
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



          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer)
              toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
          })

          Toast.fire({
            icon: 'success',
            title: 'Dados alterados com sucesso'
          })



        }
        else {
          swal("Erro!", "Erro!", "error")
            .then(() => {
              location.reload();
            })
          console.log(result);
          //swal({ title: `${result.value.userMessage.message.pt}` });
        }
      });


  }

}



//------------------------------------VALIDAR PASSWORD----------------------------------------
function valida() {
  if (document.getElementById("nPassword").validity.patternMismatch) {

    return true;
  } else {

    return false;
  }

}


var myInput420 = document.getElementById("nPassword");
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