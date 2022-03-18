window.onload = async function () {

  get_instituicoes();


  const botaoRegistar = document.getElementById("botaoRegistar");
  botaoRegistar.addEventListener("click", registar);



  //------------------------------------------------Função que faz o registo do recluso-----------------------------------------
  async function registar() {
    event.preventDefault();
    var data = {};
var rec = {};

    rec.name = document.getElementById("Rname").value.trim();
    rec.nationality = document.getElementById("nacionalidade").value.trim();
    rec.birthDate = document.getElementById("dataNascimento").value.trim();
    rec.identifierId = document.getElementById("Identificacao").value.trim();
    rec.contact = document.getElementById("contact").value.trim();
    rec.alternativeContact = document.getElementById("contactAlt").value.trim();
    rec.photo = "";
    rec.prison = { prisonId: document.getElementById("instituicao").value };
    rec.threatLevel = document.getElementById("nivel").value.trim();
    rec.cell = document.getElementById("cela").value.trim();




    if (Rname.value == "" || nacionalidade.value == "" || dataNascimento.value == "" ||
      Identificacao.value == "" || contact.value == "" || contactAlt.value == "" ||
      cela.value == "" || contact.value.length != 9 || contactAlt.value.length != 9 ||
      idpulseira.value == "") {
      Swal.fire(
        'Preencha todos os campos!',
        '',
        'warning'
      )
    } else {


      var verificar = document.getElementById("Identificacao").value.trim();
      const response = await fetch('http://127.0.0.1:8080/api/prisoners/identifier-exists/' + verificar, {

        headers: {
          'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
      });
      const existe = await response.json();


      var verificarB = document.getElementById("idpulseira").value.trim();
      const responseB = await fetch('http://127.0.0.1:8080/api/prisoners/bracelet-exists/' + verificarB, {

        headers: {
          'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
      });
      const existeB = await responseB.json();




      if (existe) {
        Swal.fire(
          'Este identificador já existe!',
          '',
          'warning'
        )
      } else {
        if (existeB) {
          Swal.fire(
            'Esta pulseira já está em uso!',
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

              if (document.getElementById("listCrimes").children.length == 0) {
                Swal.fire(
                  'Registo criminal é obrigatório!',
                  '',
                  'warning'
                )
              } else {

                if (idpulseira.value !== "") {

                  rec.braceletId = document.getElementById("idpulseira").value.trim();
                  rec.minHB = document.getElementById("mminHB").value.trim();
                  rec.maxHB = document.getElementById("mmaxHB").value.trim();
                } else {
                  rec.minHB = 40;
                  rec.maxHB = 120;
                }

                var criminal = [];
                var lista = document.getElementById("listCrimes").children;
                for (crime of lista) {
                  var elem = {}
                  elem.name = "Registo Criminal";
                  elem.description = crime.firstChild.firstChild.firstChild.innerText;
                  elem.emissionDate = crime.firstChild.firstChild.lastChild.innerText;

                  criminal.push(elem);
                }
                data.prisoner = rec;
                data.criminalRecord = criminal;

                var cuidados = [];
                if (document.getElementById("listCuidados").children.length != 0) {           
                  var listac = document.getElementById("listCuidados").children;
                  for (cuidado of listac) {
                    var elemc = {}
                    elemc.name = "Medical";
                    elemc.description = cuidado.firstChild.firstChild.innerText;
                    cuidados.push(elemc);
                  }                  
                }
                data.medicalPrescription = cuidados;


                await fetch('http://127.0.0.1:8080/api/prisoners', {
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

            }


          }


        }
      }




    }


  }



  // ---------------------------------------------Display daas instituições na ComboBox------------------------------------------------

  async function get_instituicoes() {

    let RoleLogado = localStorage.getItem("RoleLogado");
    const response1 = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'cors',
      method: 'GET',
      credentials: 'include'
    });
    const logado = await response1.json();




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


    //-----------Verificação da Role para limitar as instituições-----------
    if (RoleLogado == "ROLE_MANAGER") {
      show_inst += "<option value='" + logado.prison.prisonId + "'>" + logado.prison.name + "</option>";
    } else {
      for (var inst of instituicoes) {
        show_inst += "<option value='" + inst.prisonId + "'>" + inst.name + "</option>";
      }

    }


    document.getElementById("instituicao").innerHTML = show_inst;

  }



};

//--------------------------------------------------VALIDAÇÕES E REGEX-----------------------------------------------------


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

//----------------------------------------Slide das páginas-------------------------------------------------
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




//----------------------------------------Funções de estética que controlam as verificações--------------------------------------------

var continf = document.getElementById("contact");
var contalinf = document.getElementById("contactAlt");
var existeRec = document.getElementById("Identificacao");
var existePul = document.getElementById("idpulseira");

//-------------Informa à medida que se escreve 
continf.onkeyup = function () {
  if (document.getElementById("contact").value.length == 9 || document.getElementById("contact").value.length == 0) {
    document.getElementById("continf").style.display = "none";
  } else {
    document.getElementById("continf").style.display = "block"
  }
}

contalinf.onkeyup = function () {
  if (document.getElementById("contactAlt").value.length == 9 || document.getElementById("contactAlt").value.length == 0) {
    document.getElementById("contalinf").style.display = "none";
  } else {
    document.getElementById("contalinf").style.display = "block";
  }
}

existeRec.oninput = async function RecTaken() {

  var verificar = document.getElementById("Identificacao").value.trim();

  const response = await fetch('http://127.0.0.1:8080/api/prisoners/identifier-exists/' + verificar, {

    headers: {
      'Content-Type': 'application/json'
    },
    mode: 'cors',
    method: 'GET',
    credentials: 'include'
  });
  const existe = await response.json();

  if (existe && verificar !== "") {
    document.getElementById("existeRec").style.display = "block";
  } else {
    document.getElementById("existeRec").style.display = "none";
  }

}


existePul.oninput = async function PulTaken() {

  var verificarB = document.getElementById("idpulseira").value.trim();

  const responseB = await fetch('http://127.0.0.1:8080/api/prisoners/bracelet-exists/' + verificarB, {

    headers: {
      'Content-Type': 'application/json'
    },
    mode: 'cors',
    method: 'GET',
    credentials: 'include'
  });
  const existeB = await responseB.json();

  if (existeB && verificarB !== "") {
    document.getElementById("existePul").style.display = "block";
  } else {
    document.getElementById("existePul").style.display = "none";
  }

}



//--------------------------------------Acept image---------------------------------------------------
var pic = "";
var formData = "";
var loadFile = function (event) {

  formData = new FormData();
  formData.append("file", event.target.files[0]);
  size = ~~(event.target.files[0].size / 1024);
  pic = "a";

};


//-----------------------------------POST DA FOTOGRAFICA---------------------------------
async function post_photo(photoC, idGajo) {


  fetch('http://127.0.0.1:8080/api/prisoners/upload-photos/' + idGajo, {
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
          'Recluso registado com sucesso!',
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

//--------------------------------------------SECÇÃO DAS PULSAÇÕES--------------------------------------------
//Bloquear até inserir pulseira
document.getElementById("mminHB").addEventListener("input", function () {
  document.getElementById("minValue").innerHTML = document.getElementById("mminHB").value;
  var valor = parseInt(document.getElementById("mminHB").value);
  valor += 1;
  document.getElementById("mmaxHB").min = valor;
  document.getElementById("maxValue").innerHTML = document.getElementById("mmaxHB").value

  document.getElementById("newMin").innerHTML = "min: " + valor;
})

document.getElementById("mmaxHB").addEventListener("input", function () {
  document.getElementById("maxValue").innerHTML = document.getElementById("mmaxHB").value;
})

var inPul = document.getElementById("idpulseira");

inPul.onkeyup = function () {
  if (inPul.value.length !== 0) {
    document.getElementById("paraVer").style.opacity = 1;
    document.getElementById("mminHB").disabled = false;
    document.getElementById("mmaxHB").disabled = false;
  } else {
    document.getElementById("paraVer").style.opacity = 0.3;
    document.getElementById("mminHB").disabled = true;
    document.getElementById("mmaxHB").disabled = true;
  }
}

//--------------------------------------Adicionar crime ao registo criminal----------------------------------------------
document.getElementById("addCrime").addEventListener("click", function () {

  var texto = document.getElementById("newCrime").value.trim();
  var dataE = document.getElementById("dataEmi").value;

  if (texto !== "" && dataE != "") {
    var list = document.getElementById('listCrimes');

    var newElement = document.createElement('LI');
    var element = "";

    element += "<div class='full_tab'><div class='tab_nome'><label>" + texto + "</label><label> - </label><label>" + dataE + "</label></div>";
    element += "<button onclick=removeCrime(this) style='background-color: transparent; border: none;' class='tab_time'><i class='fas fa-trash'></i></button>";
    element += "<br></div>";

    list.appendChild(newElement);
    newElement.innerHTML = element;

    document.getElementById("newCrime").value = "";
    document.getElementById("dataEmi").value = "";
  }

})

//---------------------------------------------Remover crime do registo criminal----------------------------------------------

function removeCrime(element) {
  var apagar = element.parentNode.parentNode
  var list = document.getElementById('listCrimes');
  list.removeChild(apagar)
}


//--------------------------------------Adicionar cuidados médicos----------------------------------------------
document.getElementById("addCuidado").addEventListener("click", function () {

  var texto = document.getElementById("newCuidado").value.trim();

  if (texto !== "") {
    var list = document.getElementById('listCuidados');

    var newElement = document.createElement('LI');
    var element = "";

    element += "<div class='full_tab'><div class='tab_nome'>" + texto + "</div>";
    element += "<button onclick=removeCuidado(this) style='background-color: transparent; border: none;' class='tab_time'><i class='fas fa-trash'></i></button>";
    element += "<br></div>";

    list.appendChild(newElement);
    newElement.innerHTML = element;

    document.getElementById("newCuidado").value = "";
  }

})

//---------------------------------------------Remover cuidados médicos----------------------------------------------

function removeCuidado(element) {
  var apagar = element.parentNode.parentNode
  var list = document.getElementById('listCuidados');
  list.removeChild(apagar)
}


