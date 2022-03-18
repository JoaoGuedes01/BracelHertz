let RoleLogado1 = localStorage.getItem("RoleLogado");
let fechado = localStorage.getItem("fechado");
var listaRec = "";
$(window).on("load", function () {

    fazIsto();
    display_infoPerfil();


    if (RoleLogado1 == "ROLE_GUARD") {
        display_pulsacao();
    } else {
        var t = setTimeout(puls1Rec, 1);
    }

    if (RoleLogado1 !== "ROLE_GUARD") {

        document.getElementById("rec12").style.display = "none"
        var pi = document.getElementById("estenao");
        if (pi !== null) {
            pi.style.marginTop = "30px";
        }
    } else {
        document.getElementById("openPuls").style.display = "inline";
    }


})

//-------------------------------------------------------------AJUSTAR A BARRA DOS BATIMENTOS---------------------------------------------------------
$(document).on('scroll', function () {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance < 52) {
        var def = 95 - scrollDistance - 7;
        document.getElementById("rec12").style.top = def + "px";
    } else {
        document.getElementById("rec12").style.top = "25px";
    }
});

//---------------------------------------------------------------TROCAR CLASSES DOS ELEMENTOS---------------------------------------------------------
function trocaClasse(elemento, nova1) {
    elemento.className = "";
    elemento.classList.add(nova1);
}



//----------------------------------------------FUNÇÃO DE DISPLAY DAS PULSAÇÕES NA BARA DOS BATIMENTOS--------------------------------------
async function display_pulsacao() {


    const response = await fetch('http://127.0.0.1:8080/api/alert-prisoners', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    listaRec = await response.json();



    var tPulsacoes = document.getElementById("tPulsacoes");

    let see_puls = "";


    //criação da demonstração de resultados recebidos
    see_puls += "<ul class='teste w-100 px-0' style='list-style-type: none;'>";


    for (const rec of listaRec) {

        const response7 = await fetch('http://127.0.0.1:8080/api/photos/' + rec.photoId, {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        const photoD = await response7.json();



        see_puls += "<div class='col py-2 myfilter px-1'>";
        see_puls += "<div class='card border-left-danger shadow h-100 py-2'><div class='card-body pl-3'>";
        see_puls += "<div class='row no-gutters align-items-center'><div class='col-auto pr-2'>";
        see_puls += "<img class='picNotes40 img-profile rounded-circle' src=" + "data:image/png;base64," + photoD.picByte + ">";
        see_puls += "</div><div class='col mr-2'>";
        see_puls += "<div class='text-xs font-weight-bold text-primary text-uppercase mb-1'>" + rec.name + "</div>";
        see_puls += "<div id='" + rec.prisonerId + "puls' class='h5 mb-0 font-weight-bold' style='color: #5a5c69;'></div>";
        see_puls += "</div></div></div></div></div>";
    }
    see_puls += "</ul>";

    //envia a para a pagina
    tPulsacoes.innerHTML = see_puls;

    procurarPul();

}



//------------------------------------------------INFO PERFIL NA BARRA DE NAVEGAÇÃO-----------------------------------------


async function display_infoPerfil() {

    let RoleLogado = localStorage.getItem("RoleLogado");
    const response = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const logado = await response.json();



    const response7 = await fetch('http://127.0.0.1:8080/api/photos/' + logado.photoId, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const photoD = await response7.json();


    var nomeUser = document.getElementById("nomeUser");
    var avatarUser = document.getElementById("avatarUser");


    if (RoleLogado !== "ROLE_GUARD") {
        document.getElementById("showInst").style.display = "block";
        document.getElementById("dashBoard").href = "dashboard.html";
    }



    //envia a para a pagina
    nomeUser.innerHTML = logado.name;
    avatarUser.src = "data:image/png;base64," + photoD.picByte;


}



//-----------------------------------LOGOUT-----------------------------------------

const botaoLogout = document.getElementById("botaoLogout");

botaoLogout.addEventListener("click", sair);

async function sair() {
    event.preventDefault();
    var data = {};
    //data.email = document.getElementById("email").value;
    fetch('http://127.0.0.1:8080/api/auth/logout', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    })
        .then(function (response) {
            console.log(response);
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            return response.json();
        })
        .catch(function (err) {
            //swal.showValidationError('Pedido falhado: ' + err);
            alert(err);
        })
        .then(async function (result) {
            if (result.success) {
                Swal.fire(
                    'Sessão terminada com sucesso!',
                    '',
                    'success'
                ).then(() => {
                    localStorage.clear();
                    window.location.replace("./login.html");
                })
            }
            else {
                alert(result);
                //swal({ title: `${result.value.userMessage.message.pt}` });
            }
        });

};


//-------------------------------------------------------ASSOCIAR PULSEIRAS AOS RECLUSOS-----------------------------------------------------------------
// bracelt = ["p01", 1, 123, 12, 12345, 987654];
var bracelt = ["p01", "p02", "p03", "p04", "p05", "p06"];
var reclusos = [];


function procurarPul() {
    for (const f of listaRec) {
        for (var i = 0; i < bracelt.length; i++) {
            if (f.braceletId == bracelt[i]) {
                reclusos.push(f);
            }
        }
    }
    VerificarPulsacao();
}

//-------------------------------------Pulsação na pagina de recluso se não for guarda----------------------------------------------

function puls1Rec() {
    if (document.getElementById("pulsRec") !== null) {
        for (var br of bracelt) {
            if (br == document.getElementById("id_pulseira").value.trim()) {
        //        if (br == bracelt[0]) {
        //            var pulse = idPul;
        //        } else {
                    var pulse = Math.floor(Math.random() * parseInt(document.getElementById("maxValue").innerHTML)) + parseInt(document.getElementById("minValue").innerHTML)
        //        }
                document.getElementById("pulsRec").innerHTML = pulse;
            }
        }
    }
    var t = setTimeout(puls1Rec, 3000);
}


//--------------------------------------Pulsação na pagina de recluso e na barra lateral-------------------------------------------------
function VerificarPulsacao() {
    var ha = false;
    let cnt = 16;
    var x = document.getElementById("seeAlert");
    x.innerHTML = "";
    for (var i = 0; i < reclusos.length; i++) {

    //    if (i == 0) {
    //        var pulse = idPul;
    //    } else {
            var pulse = Math.floor(Math.random() * reclusos[i].maxHB) + reclusos[i].minHB
    //    }

        //var pulse = Math.floor(Math.random() * reclusos[i].maxHB) + reclusos[i].minHB
        document.getElementById(reclusos[i].prisonerId + "puls").innerHTML = pulse + " bpm";

        if (document.getElementById("pulsRec") !== null) {

            if (reclusos[i].braceletId == document.getElementById("id_pulseira").value.trim()) {
                document.getElementById("pulsRec").innerHTML = pulse;
            }

        }

        let arrayAlert = [];
        //if (pulse >= reclusos[i].maxHB || pulse <= reclusos[i].minHB) {
        if (pulse >= 150) {
            ha = true;
            document.getElementById(reclusos[i].prisonerId + "puls").style.color = "#e74a3b";

            let constAlert = "";

            arrayAlert.push(reclusos[i])
            for (const rec of arrayAlert) {

                constAlert += "<div style='top:" + cnt + "vh' class='snackbar show'>O recluso " + rec.name + " está em perigo</div>";
                cnt = cnt + 8;

            }

            x.innerHTML += constAlert;

            //postAlert(reclusos[i].prisonerId);

        } else {
            document.getElementById(reclusos[i].prisonerId + "puls").style.color = "#5a5c69";
        }


    } var t = setTimeout(VerificarPulsacao, 3000);



    if (x.innerHTML !== "") {
        x.innerHTML += "<button onclick='AlertaSom()' class='snackbar1 show btn btn-danger' id='StopSound' data-dismiss='modal'>Parar Alerta</button>";
    }

    if (ha) {
        //playAudio();
    } else {
        pauseAudio();
    }

}

//--------------------------------------------------------Close Alertas--------------------------------------------------------
function AlertaSom() {
    pauseAudio();
    ha = false;
    var pi = document.getElementsByClassName("snackbar");
    var x = document.getElementById("seeAlert");

    for (let teste of pi) {

        teste.classList.replace("show", "hide");
    }
    var pi2 = document.getElementById("StopSound");
    pi2.classList.replace("show", "hide");
    x.innerHTML = "";
}


var z = document.getElementById("myAudio");
function playAudio() {
    z.play();
}

function pauseAudio() {
    z.pause();
}

//-------------------------------------------------------------------------------------------------------------------------------------------------

function fazIsto() {
    var windowWidth = window.innerWidth;
    if (document.getElementById("rec12") !== null && document.getElementById("ajustarDm") !== null) {
        if (fechado == 0 || fechado == null) {

            document.getElementById("opendoor").checked = true;
            document.getElementById("opendoor2").style.backgroundColor = "transparent";
            document.getElementById("ajustarDm").style.transition = "0.5s";
            document.getElementById("rec12").style.transition = "none";
            document.getElementById("rec12").style.width = "0%";
            document.getElementById("ajustarDm").style.maxWidth = "100%";
            document.getElementById("openPuls").style.filter = "grayscale(100%)";

        } else {

            document.getElementById("opendoor2").style.backgroundColor = "transparent";
            document.getElementById("rec12").style.transition = "0.5s";
            document.getElementById("ajustarDm").style.transition = "none";
            document.getElementById("rec12").style.width = "100%";
            document.getElementById("openPuls").style.filter = "none";

            if (windowWidth <= 620) {
                document.getElementById("ajustarDm").style.maxWidth = "100%";
            } else {
                document.getElementById("ajustarDm").style.maxWidth = "83.33333%";
            }


        }
    }

}


document.getElementById("opendoor").addEventListener("change", testar)

function testar() {
    var windowWidth = window.innerWidth;
    if (document.getElementById("opendoor").checked) {
        document.getElementById("opendoor2").style.backgroundColor = "transparent";
        document.getElementById("ajustarDm").style.transition = "0.5s";
        document.getElementById("rec12").style.transition = "none";
        document.getElementById("rec12").style.width = "0%";
        document.getElementById("ajustarDm").style.maxWidth = "100%";
        document.getElementById("openPuls").style.filter = "grayscale(100%)";
        localStorage.setItem("fechado", 0);
    } else {

        document.getElementById("opendoor2").style.backgroundColor = "transparent";
        document.getElementById("rec12").style.transition = "0.5s";
        document.getElementById("ajustarDm").style.transition = "none";
        document.getElementById("rec12").style.width = "100%";
        document.getElementById("openPuls").style.filter = "none";
        if (windowWidth < 450) {
            document.getElementById("ajustarDm").style.maxWidth = "100%";
        } else {
            document.getElementById("ajustarDm").style.maxWidth = "83.33333%";
        }

        localStorage.setItem("fechado", 1);

    }
}

//-------------------------------------------------------------POST LOG---------------------------------------------------------------------

async function postAlert(recId) {

    var data = {};
    data.prisoner = { prisonerId: recId }

    await fetch('http://127.0.0.1:8080/api/alert-logs', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify(data)

    }).then(function (response) {
        if (!response.ok) {
            //alert(response);
            throw new Error("ERRO");
        }

        return response.json();
    }).then(async function (result) {

        if (result) {
            console.log("Log Criado");
            //dar reload
        }
    }).catch(function (err) {
        swal("Erro!", "", "error");
    })




}

/*
var socket = io.connect("http://localhost:3005");
var idPul;
socket.on('map', function (data) {
    socket.emit('map', data);
    idPul = data;
});
*/