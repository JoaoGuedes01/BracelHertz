$(window).on("load", function () {
    let userLogado = localStorage.getItem("userLogado");

    display_funcionarios();
    tiraBotoes();


    async function display_funcionarios() {


        var conteudo = [];

        const response = await fetch('http://127.0.0.1:8080/api/users', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        const func = await response.json();



        for (const funcionario of func) {
            if (funcionario.userId !== parseInt(userLogado)) {
                var role = "";
                if (funcionario.roles[0].id == 0) {
                    role = "Guarda Prisional";
                } else if (funcionario.roles[0].id == 1) {
                    role = "Gestor de Instituição";
                } else if (funcionario.roles[0].id == 2) {
                    role = "Gestor da Rede Prisonal";
                }

                conteudo.push(["<div id='" + funcionario.userId + "'>" + funcionario.username + "</div>",
                funcionario.name,
                funcionario.prison.name,
                funcionario.email,
                    role])
            }


        }

        $(document).ready(function () {
            $('#dataTable').DataTable({
                data: conteudo
            });
        });

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
        show_information += "<button onclick='VerApagar()' data-toggle='modal' data-target='#deleteModal' class='d-flex d-sm btn btn-sm btn-white font-weight-bold shadow-sm mr-200 he-30'> <i class='fas fa-trash fa-xs tab_time text-secondary disinline'></i></button></div>";
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



$("#tabelaFuncionarios").on('click', 'tr', function () {
    var cRow = $(this).index();
    var clicked = document.getElementById("tabelaFuncionarios").rows[cRow].firstChild.firstChild.id;
    var teste = document.getElementById("tabelaFuncionarios").rows[cRow];
    if (typeof clicked !== 'undefined') {
        if (editTabela) {
            teste.classList.toggle("selecionado");
        } else {
            location.href = "funcionario.html";
            localStorage.setItem("id_user_clicked", clicked);
            localStorage.setItem("Anot", "func");
        }
    }
});


document.getElementById("eliminar_linhas").addEventListener("click", ListaApagar)
var pode = true;
async function VerApagar() {
    pode = true;
    document.getElementById("displayList").innerHTML = "";
    ids = []
    var tabela = document.getElementById("tabelaFuncionarios");
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


            const response4 = await fetch('http://127.0.0.1:8080/api/users/' + selec.firstChild.firstChild.id, {
                headers: {
                    'Content-Type': 'application/json'
                },
                mode: 'cors',
                method: 'GET',
                credentials: 'include'
            });
            const perfil = await response4.json();


            document.getElementById("displayList").innerHTML += "<li>Username: " + perfil.username + "; Nome: " + perfil.name + "</li>";

            if (RoleLogado == "ROLE_MANAGER" && perfil.roles[0].name == "ROLE_MANAGER" || RoleLogado == "ROLE_MANAGER" && perfil.roles[0].name == "ROLE_NETWORKMAN"
                || RoleLogado == "ROLE_NETWORKMAN" && perfil.roles[0].name == "ROLE_NETWORKMAN") {

                pode = false;


            } else if (RoleLogado == "ROLE_MANAGER" && perfil.roles[0].name == "ROLE_GUARD") {

                if (perfil.prison.prisonId !== logado.prison.prisonId) {

                    pode = false;

                }
            }

        }
    }

}


async function ListaApagar() {

    if (ids.length == 0) {
        Swal.fire(
            'Seleciona pelo menos um funcionário!',
            '',
            'warning'
        )
    } else {
        if (!pode) {
            Swal.fire(
                'Apenas pode apagar funcionários da instituição a que pertence e de estatuto inferior!',
                '',
                'warning'
            )
        } else {


            for (var rec of ids) {
                var count = 0;

                fetch('http://127.0.0.1:8080/api/users/' + rec, {
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
                                    'Funcionário(os) eliminado(os) com sucesso!',
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
    var tabela = document.getElementById("tabelaFuncionarios");
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
        document.getElementById("esconderBtn").style.display = "none";
    }

}