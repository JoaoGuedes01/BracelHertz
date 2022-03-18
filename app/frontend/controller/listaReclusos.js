$(window).on("load", function () {
    let RoleLogado = localStorage.getItem("RoleLogado");
    display_reclusos();
   



    async function display_reclusos() {


        var conteudo = [];

        if (RoleLogado == "ROLE_GUARD") {
            const response = await fetch('http://127.0.0.1:8080/api/prisoners/by-guards', {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const func = await response.json();
            display(func);

        } else {
            const response = await fetch('http://127.0.0.1:8080/api/prisoners', {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const func = await response.json();
            display(func);

            document.getElementById("esconder").style.display = "flex";
            tiraBotoes();
        }

        function display(func) {

            for (const recluso of func) {

                conteudo.push(["<div id='" + recluso.prisonerId + "'>" + recluso.identifierId + "</div>",
                recluso.name,
                recluso.contact,
                recluso.prison.name,
                recluso.threatLevel,
                recluso.cell,
                recluso.braceletId])

            }

            $(document).ready(function () {
                $('#dataTable').DataTable({
                    data: conteudo
                });
            });

        }




    }


})

//----------------------------------------------------------------------------------------------------------------
var ids = [];
let RoleLogado = localStorage.getItem("RoleLogado");


var editTabela = false;
var checkbox = document.querySelector("input[name=editar_check]");
checkbox.addEventListener('change', function () {
    const showInformation = document.getElementById("show_information");
    let show_information = "";
    ids = [];
    if (this.checked) {
        show_information += "<button id='apagarRecluso' onclick='VerApagar()' data-toggle='modal' data-target='#deleteModal' class='d-flex d-sm btn btn-sm btn-white font-weight-bold shadow-sm mr-200 he-30'> <i class='fas fa-trash fa-xs tab_time text-secondary disinline'></i></button></div>";
        editTabela = true;
        document.getElementById("coisaa").innerHTML = "Cancelar";
    } else {
        ids = [];
        document.getElementById("coisaa").innerHTML = "<i class='mt-1 mr-1 fas fa-pen fa-xs tab_time text-secondary disinline'></i>Editar";
        editTabela = false;
        apaga_selecionados();
    }
    showInformation.innerHTML = show_information;
});



$("#tabelaRecluso").on('click', 'tr', function () {
    var cRow = $(this).index();
    var clicked = document.getElementById("tabelaRecluso").rows[cRow].firstChild.firstChild.id;
    var teste = document.getElementById("tabelaRecluso").rows[cRow];
    if (typeof clicked !== 'undefined') {
        if (editTabela) {
            teste.classList.toggle("selecionado");
        } else {
            location.href = "recluso.html";
            localStorage.setItem("id_user_clicked", clicked);
            localStorage.setItem("Anot", "rec");
        }
    }
});


document.getElementById("eliminar_linhas").addEventListener("click", ListaApagar)
var pode = true;
async function VerApagar() {
    pode = true;
    document.getElementById("displayList").innerHTML = "";
    ids = []
    var tabela = document.getElementById("tabelaRecluso");
    var selecionados = tabela.getElementsByClassName("selecionado");


    if (selecionados.length < 1) {
    } else {

        const response = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        const logado = await response.json();





        for (selec of selecionados) {
            ids.push(selec.firstChild.firstChild.id);

            const response4 = await fetch('http://127.0.0.1:8080/api/prisoners/' + selec.firstChild.firstChild.id, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const recluso = await response4.json();


            document.getElementById("displayList").innerHTML += "<li>Id Recluso: " + recluso.identifierId + "; Nome: " + recluso.name + "</li>";
            if (recluso.prison.prisonId !== logado.prison.prisonId && RoleLogado !== "ROLE_NETWORKMAN") {
                pode = false;
            }

        }
    }

}


async function ListaApagar() {

    if (ids.length == 0) {
        Swal.fire(
            'Seleciona pelo menos um recluso!',
            '',
            'warning'
        )
    } else {
        if (!pode) {
            Swal.fire(
                'Apenas pode apagar reclusos da instituição a que pertence!',
                '',
                'warning'
            )
        } else {


            for (var rec of ids) {
                var count = 0;

                fetch('http://127.0.0.1:8080/api/prisoners/' + rec, {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    mode: 'cors',
                    method: 'DELETE',
                    body: JSON.stringify(rec),
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
                            count++;

                            if (ids.length == count) {
                                Swal.fire(
                                    'Recluso(os) eliminado(os) com sucesso!',
                                    '',
                                    'success'
                                ).then(() => {
                                    location.reload();
                                })
                            }


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



        }
    }



}

function apaga_selecionados() {
    var tabela = document.getElementById("tabelaRecluso");
    var selecionados = tabela.getElementsByClassName("selecionado");

    var apagar = [];
    for (selec of selecionados) {
        apagar.push(selec)

    }
    for (var apaga of apagar) {
        apaga.classList.toggle("selecionado");
    }

}

function tiraBotoes() {
    var windowWidth = window.innerWidth;

    if (windowWidth <= 620) {
        document.getElementById("esconder").style.display = "none";
    }

}