window.onload = async function () {
    localStorage.clear();
    const botaoLogin = document.getElementById("botaoLogin");


    botaoLogin.addEventListener("click", entrar);


    async function entrar() {
        event.preventDefault();
        var data = {};

        data.username = document.getElementById("username").value.trim();
        data.password = document.getElementById("password").value.trim();

        fetch('http://127.0.0.1:8080/api/auth/signin', {
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
                    console.log(result);
                    localStorage.setItem("userLogado", result.userId);
                    localStorage.setItem("RoleLogado", result.role);

                    swal("Sucesso!",
                        "Autenticado com sucesso!",
                        "success")
                        .then(() => {
                            if (result.role !== "ROLE_GUARD") {
                                window.location.replace("./dashboard.html");
                            } else {
                                window.location.replace("./avisos.html");
                            }
                        })
                } else {
                    Swal.fire(
                        'Os dados que inseriu não estão corretos!',
                        '',
                        'warning'
                    )
                    console.log(result);

                }

            });

    };



};
