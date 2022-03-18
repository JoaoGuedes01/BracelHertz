let temPulseira = false;
$(window).on("load", function () {
    let id_user_clicked = localStorage.getItem("id_user_clicked");
    let RoleLogado = localStorage.getItem("RoleLogado");
    temPulseira = false;

    display_recluso();
    get_instituicoes();
    


    async function display_recluso() {



        if (RoleLogado == "ROLE_GUARD") {
            const response = await fetch('http://127.0.0.1:8080/api/prisoners/by-guards/' + id_user_clicked, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const recluso = await response.json();

            display(recluso);
        } else {
            const response = await fetch('http://127.0.0.1:8080/api/prisoners/' + id_user_clicked, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const recluso = await response.json();

            display(recluso);
        }

        //criação da demonstração de resultados recebidos
        async function display(recluso) {

            const response = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const logado = await response.json();


            /*
                        if (RoleLogado == "ROLE_MANAGER") {
                            if (recluso.prison.prisonId !== logado.prison.prisonId) {
                                document.getElementById("perfil_alterar_2").style.display = "none";
                                document.getElementById("editRegisto").style.display = "none";
                                document.getElementById("editFicha").style.display = "none";
                                document.getElementById("addRegisto").style.display = "none";
                                document.getElementById("addFicha").style.display = "none";
                                document.getElementById("naoAnotar").style.display = "none";
                                document.getElementById("switchAB").style.display = "none";
                                document.getElementById("podeMudar").style.display = "none";
                            }
                        } else {
                            if (RoleLogado !== "ROLE_NETWORKMAN") {
                                document.getElementById("editRegisto").style.display = "none";
                                document.getElementById("editFicha").style.display = "none";
                                document.getElementById("addRegisto").style.display = "none";
                                document.getElementById("addFicha").style.display = "none";
                                //document.getElementById("switchAB").style.display = "none";
                                document.getElementById("podeMudar").style.display = "none";
                            }
                        }
                        */

            if (RoleLogado == "ROLE_MANAGER" || RoleLogado == "ROLE_NETWORKMAN") {
                if (recluso.prison.prisonId == logado.prison.prisonId || RoleLogado == "ROLE_NETWORKMAN") {
                    document.getElementById("perfil_alterar_2").style.display = "inline";
                    document.getElementById("editRegisto").style.display = "inline";
                    document.getElementById("editFicha").style.display = "inline";
                    document.getElementById("addRegisto").style.display = "inline";
                    document.getElementById("addFicha").style.display = "block";
                    document.getElementById("naoAnotar").style.display = "block";
                    document.getElementById("switchAB").style.display = "block";
                    document.getElementById("podeMudar").style.display = "block";

                    document.getElementById("TabLogs").style.display = "block";
                    getLogs();
                }
            } else {
                if (RoleLogado == "ROLE_GUARD") {
                    if (recluso.prison.prisonId == logado.prison.prisonId) {
                        document.getElementById("perfil_alterar_2").style.display = "inline";
                        document.getElementById("naoAnotar").style.display = "block";
                        document.getElementById("switchAB").style.display = "block";
                        /*
                        document.getElementById("editRegisto").style.display = "inline";
                        document.getElementById("editFicha").style.display = "inline";
                        document.getElementById("addRegisto").style.display = "inline";
                        document.getElementById("addFicha").style.display = "block";
                        //document.getElementById("switchAB").style.display = "none";
                        document.getElementById("podeMudar").style.display = "block";*/
                    }
                }
            }

            //envia a para a pagina

            const response7 = await fetch('http://127.0.0.1:8080/api/photos/' + recluso.photoId, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const photoD = await response7.json();


            document.getElementById("fotoR").src = "data:image/png;base64," + photoD.picByte;
            document.getElementById("id_recluso").value = recluso.identifierId;
            document.getElementById("nome_recluso").value = recluso.name;
            document.getElementById("dn_recluso").value = recluso.birthDate;
            document.getElementById("nacionalidade_recluso").value = recluso.nationality;
            document.getElementById("contacto_recluso").value = recluso.contact;
            document.getElementById("contacto_recluso_alternativo").value = recluso.alternativeContact;
            document.getElementById("n_cela").value = recluso.cell;
            document.getElementById("n_ameaca").value = recluso.threatLevel;
            document.getElementById("id_instituicao").value = recluso.prison.prisonId;
            document.getElementById("id_pulseira").value = recluso.braceletId;
            document.getElementById("mmaxHB").value = recluso.maxHB;
            document.getElementById("mminHB").value = recluso.minHB;
            document.getElementById("maxValue").innerHTML = recluso.maxHB;
            document.getElementById("minValue").innerHTML = recluso.minHB;

            var showRegisto = "";
            var showtolipRegisto = ""

            if (recluso.criminalRecord.length == 0) {
                showRegisto = "<li>Sem registo criminal</li>";
            } else {
                var mrecente = "";
                var i = 0;
                var a, c;
                var atual = new Date();
                for (var registo of recluso.criminalRecord) {
                    showRegisto += "<li><input class='testee' id='" + registo.criminalRecordId + "cr' type='checkbox' name='" + registo.criminalRecordId + "cr' disabled='true'>"
                    showRegisto += "<div class='label'><label for='" + registo.criminalRecordId + "cr'>";
                    showRegisto += "<span data-tooltip='Emissão " + getDate7(registo.emissionDate) + "' data-tooltip-position='bottom'>" + registo.description + "</span></label><br></div></li>";
                    showRegisto += "";
                    if (i == 0) {
                        mrecente = new Date(registo.lastUpdatedTimestamp);
                        c = atual.getTime() - mrecente.getTime();
                    } else {
                        a = new Date(registo.lastUpdatedTimestamp);
                        if ((atual.getTime() - a.getTime()) < c) {
                            mrecente = new Date(registo.lastUpdatedTimestamp);
                            c = mrecente.getTime();
                        }
                    }

                    i++;
                }
                showtolipRegisto += "Registo Criminal<span data-tooltip='" + getDate6(mrecente) + "'";
                showtolipRegisto += "data-tooltip-position='bottom' class='text-white font-small font-weight-normal solve'>(Atualizado)</span>";
                document.getElementById("tabCH").innerHTML = showtolipRegisto;

            }
            document.getElementById("listCrimes").innerHTML = showRegisto;


            var showFicha = "";
            var showtolipFicha = ""
            if (recluso.medicalPrescription.length == 0) {
                showFicha = "<li>Nenhum cuidado médico necessário</li>";
            } else {
                var mrecente2 = "";
                var o = 0;
                var b, w;
                var atual = new Date();
                for (var registo of recluso.medicalPrescription) {
                    showFicha += "<li><input class='testeee' id='" + registo.prescriptionId + "fm' type='checkbox' name='" + registo.prescriptionId + "fm' disabled='true'>"
                    showFicha += "<div class='label'><label for='" + registo.prescriptionId + "fm'>" + registo.description + "</label><br></div></li>";
                    if (o == 0) {
                        mrecente2 = new Date(registo.lastUpdatedTimestamp);
                        w = atual.getTime() - mrecente2.getTime();
                    } else {
                        b = new Date(registo.lastUpdatedTimestamp);
                        if ((atual.getTime() - b.getTime()) < w) {
                            mrecente2 = new Date(registo.lastUpdatedTimestamp);
                            w = mrecente2.getTime();
                        }
                    }

                    o++;
                }

                showtolipFicha += "Cuidados a ter com o recluso<span data-tooltip='" + getDate6(mrecente2) + "'";
                showtolipFicha += "data-tooltip-position='bottom' class='text-white font-small font-weight-normal solve'>(Atualizado)</span>";
                document.getElementById("tabCH2").innerHTML = showtolipFicha;

            }
            document.getElementById("listMedica").innerHTML = showFicha;


            if (recluso.alertOff) {
                document.getElementById("notiR").checked = false;
            } else {
                document.getElementById("notiR").checked = true;
            }

            if (recluso.braceletId == null || recluso.braceletId == "") {

            } else {
                temPulseira = true;
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
        const instituicoes = await response.json();
        var show_inst = "";

        for (var inst of instituicoes) {
            show_inst += "<option value='" + inst.prisonId + "'>" + inst.name + "</option>";
        }

        document.getElementById("id_instituicao").innerHTML = show_inst;

    }


})

async function getLogs() {
    const response = await fetch('http://127.0.0.1:8080/api/prisoner-logs/' + id_user_clicked, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const logsRec = await response.json();
    var see_logs = "";
    var tableLog = document.getElementById("LogsRec");

    for (const logRec of logsRec) {
        see_logs += "<td>" + getnDate(logRec.logTimestamp) + "</td>";

        see_logs += "<td>" + logRec.description + "</td>";

        if (logRec.byUser !== null) {
            see_logs += "<td id='" + logRec.byUser.userId + "' type='button' onclick='dothis(this.id)'>" + logRec.byUser.name + " (" + logRec.byUser.username + ")</td>";
        } else {
            see_logs += "<td>Utilizador apagado</td>";
        }

        see_logs += "</tr>";
    }
    tableLog.innerHTML = see_logs;

    var t = setTimeout(getLogs, 10000);
}


//--------------------------------------EDITAR PERFIL-----------------------------------------------------
let RoleLogado = localStorage.getItem("RoleLogado");

document.getElementById("perfil_save_2").addEventListener("click", editar);
document.getElementById("notiR").addEventListener("change", editar);

async function editar() {
    event.preventDefault();
    data = {};


    let id_user_clicked = localStorage.getItem("id_user_clicked");

    if (RoleLogado == "ROLE_GUARD") {
        const response = await fetch('http://127.0.0.1:8080/api/prisoners/by-guards/' + id_user_clicked, {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        const recluso = await response.json();
        verificaGuarda(recluso);

    } else {
        const response = await fetch('http://127.0.0.1:8080/api/prisoners/' + id_user_clicked, {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        const recluso = await response.json();
        verificaOutro(recluso);
    }

    async function verificaOutro(recluso) {


        data.prisonerId = recluso.prisonerId;
        data.identifierId = document.getElementById("id_recluso").value.trim();
        data.name = document.getElementById("nome_recluso").value.trim();
        data.birthDate = document.getElementById("dn_recluso").value;
        data.nationality = document.getElementById("nacionalidade_recluso").value.trim();
        data.contact = parseInt(document.getElementById("contacto_recluso").value.trim());
        data.alternativeContact = parseInt(document.getElementById("contacto_recluso_alternativo").value.trim());
        data.cell = document.getElementById("n_cela").value.trim();
        data.threatLevel = parseInt(document.getElementById("n_ameaca").value.trim());
        data.prisonId = parseInt(document.getElementById("id_instituicao").value);
        data.braceletId = document.getElementById("id_pulseira").value.trim();
        data.minHB = document.getElementById("mminHB").value;
        data.maxHB = document.getElementById("mmaxHB").value;


        if (document.getElementById("notiR").checked) {
            data.alertOff = false;
        } else {
            data.alertOff = true;
        }




        if (document.getElementById("contacto_recluso").value == "" || document.getElementById("n_cela").value == "" ||
            document.getElementById("nacionalidade_recluso").value == "" ||
            document.getElementById("contacto_recluso_alternativo").value == "") {
            Swal.fire(
                'Preencha todos os campos!',
                '',
                'warning'
            )
        } else {

            const response9 = await fetch('http://127.0.0.1:8080/api/prisoners/' + id_user_clicked, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const recluso1 = await response9.json();





            var verificar = document.getElementById("id_recluso").value.trim();
            const response = await fetch('http://127.0.0.1:8080/api/prisoners/identifier-exists/' + verificar, {

                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const existe = await response.json();


            if (existe && recluso1.identifierId !== verificar) {
                Swal.fire(
                    'Este identificador já existe!',
                    '',
                    'warning'
                )
            } else {


                var verificarB = document.getElementById("id_pulseira").value.trim();
                const responseB = await fetch('http://127.0.0.1:8080/api/prisoners/bracelet-exists/' + verificarB, {

                    headers: {
                        'Content-Type': 'application/json'
                    },
                    mode: 'cors',
                    method: 'GET',
                    credentials: 'include'
                });
                const existeB = await responseB.json();



                if (existeB && recluso1.braceletId !== verificarB) {
                    Swal.fire(
                        'Esta pulseira já está em uso!',
                        '',
                        'warning'
                    )
                } else {
                    if (document.getElementById("contacto_recluso").value.length != 9 ||
                        document.getElementById("contacto_recluso_alternativo").value.length != 9) {
                        Swal.fire(
                            'Contacto tem de conter 9 números!',
                            '',
                            'warning'
                        )
                    } else {
                        if (document.getElementById("id_pulseira").value == "") {
                            data.minHB = 40;
                            data.maxHB = 120;
                        }

                        editarOutro(data);
                    }
                }


            }







        }

    }


    async function verificaGuarda(recluso) {


        data.prisonerId = recluso.prisonerId;
        data.cell = document.getElementById("n_cela").value.trim();
        data.braceletId = document.getElementById("id_pulseira").value.trim();
        data.minHB = document.getElementById("mminHB").value;
        data.maxHB = document.getElementById("mmaxHB").value;

        if (document.getElementById("notiR").checked) {
            data.alertOff = false;
        } else {
            data.alertOff = true;
        }




        if (document.getElementById("n_cela").value == "") {
            Swal.fire(
                'Preencha todos os campos!',
                '',
                'warning'
            )
        } else {

            const response9 = await fetch('http://127.0.0.1:8080/api/prisoners/by-guards/' + id_user_clicked, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const recluso1 = await response9.json();


            var verificarB = document.getElementById("id_pulseira").value.trim();
            const responseB = await fetch('http://127.0.0.1:8080/api/prisoners/bracelet-exists/' + verificarB, {

                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const existeB = await responseB.json();


            if (existeB && recluso1.braceletId !== verificarB) {
                Swal.fire(
                    'Esta pulseira já está em uso!',
                    '',
                    'warning'
                )
            }else{


                if (document.getElementById("id_pulseira").value == "") {

                    data.minHB = 40;
                    data.maxHB = 120;
                }
    
                editarGuarda(data);




            }





        }

    }



}


async function editarGuarda(gajo) {





    fetch('http://127.0.0.1:8080/api/prisoners/by-guards', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'PUT',
        body: JSON.stringify(gajo),
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
                //swal({ title: "Autenticação feita com sucesso!" });
                //+ result.value.message.success);S




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
                    title: 'Alterada com sucesso'
                }).then(() => {
                    // display_recluso();
                    Myfunction425();
                    if (RoleLogado == "ROLE_GUARD") {
                        display_pulsacao();
                    }
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

            }
        });


}

async function editarOutro(gajo) {





    fetch('http://127.0.0.1:8080/api/prisoners', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'PUT',
        body: JSON.stringify(gajo),
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
                //swal({ title: "Autenticação feita com sucesso!" });
                //+ result.value.message.success);S




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
                    title: 'Alterada com sucesso'
                }).then(() => {
                    // display_recluso();
                    Myfunction425();
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

            }
        });


}









//----------------------------------------------------------------------------------------------------------------

function checkInp() {
    var x = document.getElementById("newNif").value;
    if ((x % 1) != 0) {
        //alert("So aceita numeros");
        return false;
    } else {
        return true;
    }
}


//(v-vmin)*90/(vmax-vmin)

//-------------------------------------------------------------------------------------------------------


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
//----------Só aceita letras e um espaço e pontos, virgulas----- regex-----------
$('.1spaceand').keyup(function () {
    var $th = $(this);
    $th.val($th.val().replace(/(\s{2,})|[^a-zA-Zà-úÀ-Ú\d.,!?()$€ªº:@_\-']/g, ' '));
    $th.val($th.val().replace(/^\s*/, ''));
})




//--------------------------------------------------------------------------------


document.getElementById("perfil_alterar_2").addEventListener("click", function () {
    let RoleLogado = localStorage.getItem("RoleLogado");
    if (RoleLogado == "ROLE_GUARD") {
        Myfunction4245();
    } else {
        Myfunction424();
        if (RoleLogado == "ROLE_NETWORKMAN") {
            document.getElementById("id_instituicao").disabled = false;
            document.getElementById("icon_id_instituicao").style.display = "block";
        }
    }


    if (temPulseira) {
        myfunction1234()
    }

});



function myfunction1234() {
    document.getElementById("showMin").style.opacity = 1;
    document.getElementById("showMax").style.opacity = 1;
    document.getElementById("mminHB").disabled = false;
    document.getElementById("mmaxHB").disabled = false;
}


function Myfunction424() {
    document.getElementById("id_pulseira").readOnly = false;
    document.getElementById("icon_id_pulseira").style.display = "block";
    document.getElementById("nacionalidade_recluso").readOnly = false;
    document.getElementById("icon_nacionalidade_recluso").style.display = "block";
    document.getElementById("contacto_recluso").readOnly = false;
    document.getElementById("icon_contacto_recluso").style.display = "block";
    document.getElementById("contacto_recluso_alternativo").readOnly = false;
    document.getElementById("icon_contacto_recluso_alternativo").style.display = "block";
    document.getElementById("n_cela").readOnly = false;
    document.getElementById("icon_n_cela").style.display = "block";
    document.getElementById("n_ameaca").disabled = false;
    document.getElementById("icon_n_ameaca").style.display = "block";
    document.getElementById("perfil_alterar_2").style.display = "none";
    document.getElementById("perfil_save_2").style.display = "block";
    document.getElementById("dn_recluso").readOnly = false;
    document.getElementById("nome_recluso").readOnly = false;
    document.getElementById("icon_nome_recluso").style.display = "block";
    document.getElementById("id_recluso").readOnly = false;
    document.getElementById("icon_id_recluso").style.display = "block";


}


function Myfunction4245() {
    document.getElementById("id_pulseira").readOnly = false;
    document.getElementById("icon_id_pulseira").style.display = "block";

    document.getElementById("n_cela").readOnly = false;
    document.getElementById("icon_n_cela").style.display = "block";


    document.getElementById("perfil_alterar_2").style.display = "none";
    document.getElementById("perfil_save_2").style.display = "block";
}

function Myfunction425() {
    document.getElementById("id_pulseira").readOnly = true;
    document.getElementById("icon_id_pulseira").style.display = "none";
    document.getElementById("id_instituicao").disabled = true;
    document.getElementById("icon_id_instituicao").style.display = "none";
    document.getElementById("nacionalidade_recluso").readOnly = true;
    document.getElementById("icon_nacionalidade_recluso").style.display = "none";
    document.getElementById("contacto_recluso").readOnly = true;
    document.getElementById("icon_contacto_recluso").style.display = "none";
    document.getElementById("contacto_recluso_alternativo").readOnly = true;
    document.getElementById("icon_contacto_recluso_alternativo").style.display = "none";
    document.getElementById("n_cela").readOnly = true;
    document.getElementById("icon_n_cela").style.display = "none";
    document.getElementById("n_ameaca").disabled = true;
    document.getElementById("icon_n_ameaca").style.display = "none";
    document.getElementById("perfil_save_2").style.display = "none";
    document.getElementById("perfil_alterar_2").style.display = "block";
    document.getElementById("dn_recluso").readOnly = true;
    document.getElementById("nome_recluso").readOnly = true;
    document.getElementById("icon_nome_recluso").style.display = "none";
    document.getElementById("id_recluso").readOnly = true;
    document.getElementById("icon_id_recluso").style.display = "none";

    document.getElementById("showMin").style.opacity = 0.3;
    document.getElementById("showMax").style.opacity = 0.3;
    document.getElementById("mminHB").disabled = true;
    document.getElementById("mmaxHB").disabled = true;
}


//-------------------------------------------------------------------------------------------
document.getElementById("mminHB").addEventListener("input", function () {
    document.getElementById("minValue").innerHTML = document.getElementById("mminHB").value;
    var valor = parseInt(document.getElementById("mminHB").value);
    valor += 1;
    document.getElementById("mmaxHB").min = valor;
    document.getElementById("maxValue").innerHTML = document.getElementById("mmaxHB").value

})

document.getElementById("mmaxHB").addEventListener("input", function () {
    document.getElementById("maxValue").innerHTML = document.getElementById("mmaxHB").value;
})


//------------------------------------------UPLOAD PHOTO------------------------------------------------------
let id_user_clicked = localStorage.getItem("id_user_clicked");

var loadFile = function (event) {
    var image = document.getElementById('fotoR');
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


        fetch('http://127.0.0.1:8080/api/prisoners/upload-photos/' + id_user_clicked, {
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

//------------------------------------------MUDAR CLASSES----------------------------------------------
function trocaClasse(elemento, nova1) {
    elemento.className = "";
    elemento.classList.add(nova1);
}

function trocaClasse2(elemento, nova1, nova2) {
    elemento.className = "";
    elemento.classList.add(nova1);
    elemento.classList.add(nova2);
}
//---------------------------------------------GET IDS------------------------------------------------------
let ids = [];
function getIds() {
    ids = [];
    const selecList = document.getElementById("listCrimes");
    for (let elem of selecList.children) {
        if (elem.firstChild.checked) {
            var mete = elem.firstChild.id.replace('cr', '');
            ids.push(mete.trim());

        }
    }

    return ids
}

function getIds2() {
    ids = [];
    const selecList = document.getElementById("listMedica");
    for (let elem of selecList.children) {
        if (elem.firstChild.checked) {
            var mete = elem.firstChild.id.replace('fm', '');
            ids.push(mete.trim());
        }
    }

    return ids
}

//-------------------------------------------------DATA Formato------------------------------------------
function getDate5(date) {
    var today = new Date(date);
    var d = today.getDate();
    var mo = today.getMonth()
    var a = today.getFullYear();
    d = checkTime(d);
    mo = checkTime(mo + 1);
    return a + "-" + mo + "-" + d;
}
function checkTime(i) {
    if (i < 10) { i = "0" + i };  // add zero in front of numbers < 10
    return i;
}

function getDate6(date) {
    var dias = ["Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"];
    var today = new Date(date);
    var dia = today.getDay();
    var d = today.getDate();
    var mo = today.getMonth();
    var a = today.getFullYear();
    var h = today.getHours();
    var m = today.getMinutes();
    h = checkTime(h);
    m = checkTime(m);
    d = checkTime(d);
    mo = checkTime(mo + 1);
    return dias[dia] + " " + d + " " + mo + " " + a + " " + h + ":" + m + "h";
}

function getDate7(date) {
    var today = new Date(date);
    var d = today.getDate();
    var mo = today.getMonth()
    var a = today.getFullYear();
    d = checkTime(d);
    mo = checkTime(mo + 1);
    return d + "/" + mo + "/" + a;
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

//---------------------------------------------REGISTO CRIMINAL----------------------------------------------
var clickteste = false;
$('#editRegisto').click(function () {


    $('.testee').each(function () {
        if (!clickteste) {
            this.disabled = false;
            trocaClasse2(document.getElementById("editRegisto"), "fas", "fa-times");
            trocaClasse(document.getElementById("tabCH"), "tab_nome94");
            document.getElementById("trashRegisto").style.display = "inline";

        } else {
            this.checked = false;
            this.disabled = true;
            document.getElementById("trashRegisto").style.display = "none";
            trocaClasse(document.getElementById("tabCH"), "tab_nome");
            trocaClasse2(document.getElementById("editRegisto"), "fas", "fa-pen");
        }
    });

    //switch
    if (clickteste) {
        clickteste = false;
    } else {
        clickteste = true;
    }

});

document.getElementById("trashRegisto").addEventListener("click", function () {
    getIds();

    if (ids == "") {
        Swal.fire(
            'Seleciona pelo menos um registo!',
            '',
            'warning'
        )
    } else {
        for (let id of ids) {
            eliminar(id);
        }

    }

})


//---------------------------------------------ELIMINAR REGISTO CRIMINAL------------------------
async function eliminar(id) {
    fetch('http://127.0.0.1:8080/api/criminal-records/' + id, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'DELETE',
        body: JSON.stringify(id),
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
                    title: 'Alterada com sucesso'
                }).then(() => {
                    location.reload();
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
}


//-----------------------------------------------POST DE REGISTO CRIMINAL------------------------------------------------

document.getElementById("postRegisto").addEventListener("click", async function () {

    if (document.getElementById("novoRegisto").value.trim() == "") {
        Swal.fire(
            'Insira algo novo a registar!',
            '',
            'warning'
        )
    } else {

        if (document.getElementById("dataEmissao").value == "") {
            Swal.fire(
                'Data de emissão obrigatória!',
                '',
                'warning'
            )
        } else {

            var data = {}

            data.prisoner = { prisonerId: id_user_clicked };
            data.name = "Registo Criminal";
            data.description = document.getElementById("novoRegisto").value.trim();

            data.emissionDate = document.getElementById("dataEmissao").value;






            await fetch('http://127.0.0.1:8080/api/criminal-records', {
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

                return response.json();
            }).then(async function (result) {

                if (result) {

                    const Toast = Swal.mixin({
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 500,
                        timerProgressBar: true,
                        onOpen: (toast) => {
                            toast.addEventListener('mouseenter', Swal.stopTimer)
                            toast.addEventListener('mouseleave', Swal.resumeTimer)
                        }
                    })

                    Toast.fire({
                        icon: 'success',
                        title: 'Adicionado com sucesso'
                    })
                        .then(() => {
                            location.reload();
                        })


                }
            }).catch(function (err) {
                swal("Erro!", err, "error");
            })



        }

    }

})



//---------------------------------------------Ficha Médica----------------------------------------------
var clickteste2 = false;
$('#editFicha').click(function () {

    $('.testeee').each(function () {
        if (!clickteste2) {
            this.disabled = false;
            trocaClasse2(document.getElementById("editFicha"), "fas", "fa-times");
            trocaClasse(document.getElementById("tabCH2"), "tab_nome94");
            document.getElementById("trashFicha").style.display = "inline";

        } else {
            this.checked = false;
            this.disabled = true;
            document.getElementById("trashFicha").style.display = "none";
            trocaClasse(document.getElementById("tabCH2"), "tab_nome");
            trocaClasse2(document.getElementById("editFicha"), "fas", "fa-pen");
        }
    });

    //switch
    if (clickteste2) {
        clickteste2 = false;
    } else {
        clickteste2 = true;
    }

});

document.getElementById("trashFicha").addEventListener("click", function () {
    getIds2();

    if (ids == "") {
        Swal.fire(
            'Seleciona pelo menos um registo!',
            '',
            'warning'
        )
    } else {
        for (let id of ids) {
            eliminarFicha(id);
        }

    }

})


//---------------------------------------------ELIMINAR REGISTO MEDICO------------------------
async function eliminarFicha(id) {
    fetch('http://127.0.0.1:8080/api/medical-prescriptions/' + id, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'DELETE',
        body: JSON.stringify(id),
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
                    title: 'Alterada com sucesso'
                }).then(() => {
                    location.reload();
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
}


//-----------------------------------------------POST DE FICHA MEDICA------------------------------------------------

document.getElementById("postFicha").addEventListener("click", async function () {

    if (document.getElementById("novaFicha").value.trim() == "") {
        Swal.fire(
            'Insira algo novo a registar!',
            '',
            'warning'
        )
    } else {

        var data = {}

        data.prisoner = { prisonerId: parseInt(id_user_clicked) };
        data.name = "Medical";
        data.description = document.getElementById("novaFicha").value.trim();


        await fetch('http://127.0.0.1:8080/api/medical-prescriptions', {
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

            return response.json();
        }).then(async function (result) {

            if (result) {

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 500,
                    timerProgressBar: true,
                    onOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                })

                Toast.fire({
                    icon: 'success',
                    title: 'Adicionado com sucesso'
                })
                    .then(() => {
                        location.reload();
                    })


            }
        }).catch(function (err) {
            swal("Erro!", err, "error");
        })





    }

})






