let RoleLogado = localStorage.getItem("RoleLogado");
let id_user_clicked = localStorage.getItem("id_user_clicked");
let userLogado = localStorage.getItem("userLogado");
$(window).on("load", function () {

    startTime();
    display_logs();
    display_info();
    renderOcorrencias();
    renderOcorrencias_recluso();

})

async function display_logs() {

    var tabelBody = document.getElementById("tabelBody");

    let see_logs = "";
    let listaLogG = "";
    let listaLogR = "";


    if (RoleLogado == "ROLE_MANAGER") {
        const response = await fetch('http://127.0.0.1:8080/api/user-logs/managers', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        listaLogG = await response.json();



        const response1 = await fetch('http://127.0.0.1:8080/api/prisoner-logs/managers', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        listaLogR = await response1.json();



    } else if (RoleLogado == "ROLE_NETWORKMAN") {
        const response = await fetch('http://127.0.0.1:8080/api/user-logs', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        listaLogG = await response.json();


        const response1 = await fetch('http://127.0.0.1:8080/api/prisoner-logs', {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            method: 'GET',
            credentials: 'include'
        });
        listaLogR = await response1.json();


    }
    //criação da demonstração de resultados recebidos
    if (document.getElementById("tipoDest").value == 1) {

        for (const logG of listaLogG) {

            see_logs += "<td>" + getDate(logG.logTimestamp) + "</td>";

            if (logG.byUser !== null) {
                see_logs += "<td id='" + logG.byUser.userId + "' type='button' onclick='dothis(this.id)'>" + logG.byUser.name + " (" + logG.byUser.username + ")</td>";
            } else {
                see_logs += "<td>Utilizador apagado</td>";
            }

            see_logs += "<td>" + logG.description + "</td>";

            if (logG.user == null) {
                see_logs += "<td>Utilizador apagada</td>";
            } else {
                see_logs += "<td id='" + logG.user.userId + "' type='button' onclick='dothis(this.id)'>" + logG.user.name + " (" + logG.user.username + ")</td>";
            }

            if (logG.user == null) {
                see_logs += "<td></td>";
            } else {
                see_logs += "<td id='" + logG.user.prison.prisonId + "' type='button' onclick='dothis2(this.id)'>" + logG.user.prison.name + "</td>";
            }

            see_logs += "</tr>";
        }

    } else {

        for (const logR of listaLogR) {

            see_logs += "<td>" + getDate(logR.logTimestamp) + "</td>";

            if (logR.byUser !== null) {
                see_logs += "<td id='" + logR.byUser.userId + "' type='button' onclick='dothis(this.id)'>" + logR.byUser.name + " (" + logR.byUser.username + ")</td>";
            } else {
                see_logs += "<td>Utilizador apagado</td>";
            }

            see_logs += "<td>" + logR.description + "</td>";

            if (logR.prisoner !== null) {
                see_logs += "<td id='" + logR.prisoner.prisonerId + "' type='button' onclick='dothat(this.id)'>" + logR.prisoner.name + " (" + logR.prisoner.identifierId + ")</td>";
            } else {
                see_logs += "<td>Reclusos apagado</td>";
            }

            if (logR.prisoner !== null) {
                see_logs += "<td id='" + logR.prisoner.prison.prisonId + "' type='button' onclick='dothis2(this.id)'>" + logR.prisoner.prison.name + "</td>";
            } else {
                see_logs += "<td></td>";
            }

            see_logs += "</tr>";

        }

    }
    //envia a para a pagina
    tabelBody.innerHTML = see_logs;




    var t = setTimeout(display_logs, 10000);
}


document.getElementById("tipoDest").addEventListener("change", display_logs);


//--------------------------------------vai para as paginas-------------------------------------------

function dothis(id) {
    if (id == userLogado) {
        location.href = "perfil.html";
    } else {
        localStorage.setItem("id_user_clicked", id);
        localStorage.setItem("Anot", "func");
        location.href = "funcionario.html";
    }

}

function dothis2(id) {
    localStorage.setItem("id_inst_clicked", id);
    localStorage.setItem("Anot", "inst");
    location.href = "instituicao.html";
}

function dothat(id) {
    localStorage.setItem("id_user_clicked", id);
    localStorage.setItem("Anot", "rec");
    location.href = "recluso.html";
}





//------------------------------------DISPLAY INFO-------------------------------------------------


async function display_info() {

    const response = await fetch('http://127.0.0.1:8080/api/dashboard', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const info = await response.json();


    var nOcorrencias = document.getElementById("nOcorrencias");
    var nFuncionarios = document.getElementById("nFuncionarios");
    var nReclusos = document.getElementById("nReclusos");

    //envia a para a pagina
    nOcorrencias.innerHTML = info.totalAlerts;
    nFuncionarios.innerHTML = info.totalUsers;
    nReclusos.innerHTML = info.totalPrisoners;


}

//-----------------------------------------RENDER OCORRENCIAS-----------------------------------------------------------------


async function renderOcorrencias() {
    const response = await fetch('http://127.0.0.1:8080/api/alert-logs', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });

    const gr1 = await response.json();


    var teste = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    var final = [];
    //var final2 = [10, 4, 5, 8, 0, 12, 7];
    var today = new Date();
    today = today.getMonth();

    for (const f of gr1) {

        switch (new Date(f.createdTimestamp).getMonth()) {

            case 0:
                final[0] = teste[0] + 1;
                teste[0] = teste[0] + 1;
                break;
            case 1:
                final[0] = teste[0];
                final[1] = teste[1] + 1;
                teste[1] = teste[1] + 1;
                break;
            case 2:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2] + 1;
                teste[2] = teste[2] + 1;
                break;
            case 3:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3] + 1;
                teste[3] = teste[3] + 1;
                break;
            case 4:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4] + 1;
                teste[4] = teste[4] + 1;
                break;
            case 5:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5] + 1;
                teste[5] = teste[5] + 1;
                break;
            case 6:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5];
                final[6] = teste[6] + 1;
                teste[6] = teste[6] + 1;
                break;
            case 7:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5];
                final[6] = teste[6];
                final[7] = teste[7] + 1;
                teste[7] = teste[7] + 1;
                break;
            case 8:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5];
                final[6] = teste[6];
                final[7] = teste[7];
                final[8] = teste[8] + 1;
                teste[8] = teste[8] + 1;
                break;
            case 9:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5];
                final[6] = teste[6];
                final[7] = teste[7];
                final[8] = teste[8];
                final[9] = teste[9] + 1;
                teste[9] = teste[9] + 1;
                break;
            case 10:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5];
                final[6] = teste[6];
                final[7] = teste[7];
                final[8] = teste[8];
                final[9] = teste[9];
                final[10] = teste[10] + 1;
                teste[10] = teste[10] + 1;
                break;
            case 11:
                final[0] = teste[0];
                final[1] = teste[1];
                final[2] = teste[2];
                final[3] = teste[3];
                final[4] = teste[4];
                final[5] = teste[5];
                final[6] = teste[6];
                final[7] = teste[7];
                final[8] = teste[8];
                final[9] = teste[9];
                final[10] = teste[10];
                final[11] = teste[11] + 1;
                teste[11] = teste[11] + 1;
                break;
        }

    }

    poe(final);
}








function poe(dados) {

    // Area Chart Example
    var ctx = document.getElementById("myAreaChart");
    var myLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["Jan", "Feb", "Mar", "Abr", "Maio", "Jun", "Jul", "Aug", "Set", "Out", "Nov", "Dez"],
            datasets: [{
                label: "Ocorrências",
                lineTension: 0.3,
                backgroundColor: "rgba(78, 115, 223, 0.05)",
                borderColor: "#1b2c47",
                pointRadius: 3,
                pointBackgroundColor: "#0e1e37",
                pointBorderColor: "#0e1e37",
                pointHoverRadius: 3,
                pointHoverBackgroundColor: "#d57201",
                pointHoverBorderColor: "#d57201",
                pointHitRadius: 10,
                pointBorderWidth: 2,
                //data: [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
                data: dados,
            }],
        },
        options: {
            maintainAspectRatio: false,
            layout: {
                padding: {
                    left: 10,
                    right: 25,
                    top: 25,
                    bottom: 0
                }
            },
            scales: {
                xAxes: [{
                    time: {
                        unit: 'date'
                    },
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    ticks: {
                        maxTicksLimit: 7
                    }
                }],
                yAxes: [{
                    ticks: {
                        maxTicksLimit: 5,
                        padding: 10,
                        // Include a dollar sign in the ticks
                        callback: function (value, index, values) {
                            return number_format(value);
                        }
                    },
                    gridLines: {
                        color: "rgb(234, 236, 244)",
                        zeroLineColor: "rgb(234, 236, 244)",
                        drawBorder: false,
                        borderDash: [2],
                        zeroLineBorderDash: [2]
                    }
                }],
            },
            legend: {
                display: false
            },
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                titleMarginBottom: 10,
                titleFontColor: '#6e707e',
                titleFontSize: 14,
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                intersect: false,
                mode: 'index',
                caretPadding: 10,
                callbacks: {
                    label: function (tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel + ': ' + number_format(tooltipItem.yLabel);
                    }
                }
            }
        }
    });
}

//-------------------------------------------------------------------------------------

async function renderOcorrencias_recluso() {


    const response = await fetch('http://127.0.0.1:8080/api/dashboard-circle', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });

    const gr2 = await response.json();

    const valores = [gr2.totalPrisonersWithNoAlerts, gr2.totalPrisonersWithAlerts]
    poeGrafico(valores);
}

function poeGrafico(valores) {
    // Set new default font family and font color to mimic Bootstrap's default styling
    Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
    Chart.defaults.global.defaultFontColor = '#858796';

    // Pie Chart Example
    var ctx = document.getElementById("myPieChart");
    var myPieChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ["Sem Ocorrências", "Com Ocorrências"],
            datasets: [{
                data: valores,
                backgroundColor: ['#1b2c47', '#ff8800'],
                hoverBackgroundColor: ['#0e1e37', '#e57a00'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }],
        },
        options: {
            maintainAspectRatio: false,
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                caretPadding: 10,
            },
            legend: {
                display: false
            },
            cutoutPercentage: 80,
        },
    });


}

//-------------------------------------------------------------------------------------



function startTime() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    if (document.getElementById("txt") != null) {
        document.getElementById('txt').innerHTML =
            h + ":" + m + "h";
        var t = setTimeout(startTime, 500);
    }
}
function checkTime(i) {
    if (i < 10) { i = "0" + i };  // add zero in front of numbers < 10
    return i;
}



//-------------------------------------------------DATA FORMAT---------------------------------------------------------
function getDate(date) {
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


//-----------------------------------------------------A VER COM O GRAFICO-----------------------------------------------------



// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

function number_format(number, decimals, dec_point, thousands_sep) {
    // *     example: number_format(1234.56, 2, ',', ' ');
    // *     return: '1 234,56'
    number = (number + '').replace(',', '').replace(' ', '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.round(n * k) / k;
        };
    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}



//----------------------------------------------------------------------------------------------------------------------------------