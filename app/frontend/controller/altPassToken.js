document.getElementById("botaoEnvSMS").addEventListener("click", function () {

    const username = document.getElementById("username").value.trim();

    if (username == "") {
        Swal.fire(
            'Insira o username',
            '',
            'warning'
        )
    } else {

        const data = {};

        data.username = username;


        fetch('http://127.0.0.1:8080/api/sms', {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(data),
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
                console.log(err);
            })
            .then(async function (result) {
                if (result) {
                    
                    swal("Sucesso!",
                        "Mensagem enviada!",
                        "success")
                        .then(() => {

                            document.getElementById("NewPass").style.display = "block";
                            document.getElementById("username").disabled = true;

                        })

                } else {
                    swal("Erro!", "Erro!", "error");
                    console.log(result);

                }

            });


    }


})



document.getElementById("botaoRecPass").addEventListener("click", function () {

    const token = document.getElementById("token").value.trim();
    const pass = document.getElementById("password").value.trim();

    if (token == "" || pass == "") {
        Swal.fire(
            'Preencha todos os campos',
            '',
            'warning'
        )
    } else {

        const data1 = {};
        data1.username = document.getElementById("username").value.trim();
        data1.token = token;
        data1.newPassword = pass;


        fetch('http://127.0.0.1:8080/api/users/network-managers/forgotten-passwords', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'PUT',
            body: JSON.stringify(data1),
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

                console.log(err);
            })
            .then(async function (result) {
                console.log(result);
                if (result) {
                    swal("Sucesso!",
                        "Password alterada com sucesso!",
                        "success")
                        .then(() => {

                            window.location.replace("./login.html");

                        })
                }
                else {
                    Swal.fire(
                        'Ocorreu um erro!',
                        '',
                        'error'
                    )
                    console.log(result);

                }
            });



    }

})