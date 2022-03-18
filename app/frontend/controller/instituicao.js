let RoleLogado = localStorage.getItem("RoleLogado");
let id_inst_clicked = localStorage.getItem("id_inst_clicked");
$(window).on("load", function () {

    display_instituicao();
})


async function display_instituicao() {


    if (RoleLogado == "ROLE_NETWORKMAN") {
        document.getElementById("perfil_alterar_2").style.display = "block";
        document.getElementById("EditFoto").style.display = "block";
    }



    const response = await fetch('http://127.0.0.1:8080/api/prisons/' + id_inst_clicked, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const instituicao = await response.json();

    const response7 = await fetch('http://127.0.0.1:8080/api/photos/' + instituicao.photoId, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const photoD = await response7.json();

    //envia a para a pagina
    document.getElementById("fotoinst").src = "data:image/png;base64," + photoD.picByte;
    document.getElementById("nome_inst").value = instituicao.name;
    document.getElementById("morada_inst").value = instituicao.address;
    document.getElementById("local_inst").value = instituicao.location;
    document.getElementById("email_inst").value = instituicao.email;
    document.getElementById("contacto_inst").value = instituicao.contact;
    document.getElementById("prisonDescrip").value = instituicao.description;


    const response5 = await fetch('http://127.0.0.1:8080/api/users/logged-profiles', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const logado = await response5.json();

    if (logado.prison.prisonId == instituicao.prisonId && RoleLogado !== "ROLE_NETWORKMAN" || RoleLogado == "ROLE_NETWORKMAN") {
        document.getElementById("naoAnotar").style.display = "block";
    }
    if(RoleLogado == "ROLE_NETWORKMAN"){
        document.getElementById("podeMudar").style.display="block";
    }


}



//------------------------------------------------EDITAR INSTITUIÇÃO-----------------------------------------------------

document.getElementById("perfil_save_2").addEventListener("click", editar);

async function editar() {
    event.preventDefault();

    data = {};


    const response = await fetch('http://127.0.0.1:8080/api/prisons/' + id_inst_clicked, {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const instituicao = await response.json();



    data.prisonId = instituicao.prisonId;
    data.name = document.getElementById("nome_inst").value.trim();
    data.location = document.getElementById("local_inst").value.trim();
    data.address = document.getElementById("morada_inst").value.trim();
    data.contact = document.getElementById("contacto_inst").value.trim();
    data.email = document.getElementById("email_inst").value.trim();
    data.description = document.getElementById("prisonDescrip").value.trim();
    data.photoId = instituicao.photoId;





    if (document.getElementById("nome_inst").value.trim() == "" || document.getElementById("local_inst").value.trim() == "" ||
        document.getElementById("morada_inst").value.trim() == "" || document.getElementById("contacto_inst").value.trim() == "" ||
        document.getElementById("email_inst").value.trim() == "" || document.getElementById("prisonDescrip").value.trim() == "" ||
        document.getElementById("contacto_inst").value.length !== 9) {

        Swal.fire(
            'Preencha todos os campos!',
            '',
            'warning'
        )

    } else {
        if (validacaoEmail(document.getElementById("email_inst"))) {




            fetch('http://127.0.0.1:8080/api/prisons', {
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




        } else {
            Swal.fire(
                'Este email não é válido!',
                '',
                'warning'
            )
        }


    }


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

//------------------------------------------------------------------------------------------------------------

document.getElementById("perfil_alterar_2").addEventListener("click", Myfunction424);

function myfunction420() {
    document.getElementById("prisonDescrip").readOnly = false;
    document.getElementById("prisonDescrip").style.border = "groove";
}

function myfunction421() {
    document.getElementById("prisonDescrip").readOnly = true;
    document.getElementById("prisonDescrip").style.border = "hidden";
}


function Myfunction424() {
    document.getElementById("nome_inst").readOnly = false;
    document.getElementById("icon_nome_inst").style.display = "block";
    document.getElementById("morada_inst").readOnly = false;
    document.getElementById("icon_morada_inst").style.display = "block";
    document.getElementById("local_inst").readOnly = false;
    document.getElementById("icon_local_inst").style.display = "block";
    document.getElementById("email_inst").readOnly = false;
    document.getElementById("icon_email_inst").style.display = "block";
    document.getElementById("contacto_inst").readOnly = false;
    document.getElementById("icon_contacto_inst").style.display = "block";
    document.getElementById("prisonDescrip").readOnly = false;
    document.getElementById("icon_prisonDescrip").style.display = "block";
    document.getElementById("perfil_alterar_2").style.display = "none";
    document.getElementById("perfil_save_2").style.display = "block";

}

function Myfunction425() {
    document.getElementById("nome_inst").readOnly = true;
    document.getElementById("icon_nome_inst").style.display = "none";
    document.getElementById("morada_inst").readOnly = true;
    document.getElementById("icon_morada_inst").style.display = "none";
    document.getElementById("local_inst").readOnly = true;
    document.getElementById("icon_local_inst").style.display = "none";
    document.getElementById("email_inst").readOnly = true;
    document.getElementById("icon_email_inst").style.display = "none";
    document.getElementById("contacto_inst").readOnly = true;
    document.getElementById("icon_contacto_inst").style.display = "none";
    document.getElementById("prisonDescrip").readOnly = true;
    document.getElementById("icon_prisonDescrip").style.display = "none";
    document.getElementById("perfil_save_2").style.display = "none";
    document.getElementById("perfil_alterar_2").style.display = "block";

}


//------------------------------------------UPLOAD PHOTO------------------------------------------------------


var loadFile = function (event) {
    var image = document.getElementById('fotoinst');
    image.src = URL.createObjectURL(event.target.files[0]);

    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    size = ~~(event.target.files[0].size / 1024);

    editar_photo(formData);


};

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

        fetch('http://127.0.0.1:8080/api/prisons/upload-photos/' + id_inst_clicked, {
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
                    Swal.fire(
                        'Ocorreu um erro!',
                        'Foto apenas pode ter até 1 MB inclusive',
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



