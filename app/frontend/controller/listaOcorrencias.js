$(window).on("load", function () {

    display_ocorrencias();
})
var last10 = [];
async function display_ocorrencias() {

    var conteudo = [];

    const response = await fetch('http://127.0.0.1:8080/api/alert-logs', {
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors',
        method: 'GET',
        credentials: 'include'
    });
    const func = await response.json();


    var today = new Date().getTime();

    for (var i = 0; i < func.length; i++) {
        var ver = new Date(func[i].createdTimestamp).getTime();


        conteudo.push([func[i].prisoner.identifierId,
        func[i].prisoner.name,
        func[i].prisoner.prison.name,
        getDate6(func[i].createdTimestamp)])


        if (today - ver <= 600000) {
            last10.push(i);
        }


    }

    $(document).ready(function () {
        $('#dataTable').DataTable({
            data: conteudo,
            "order": [[3, "desc"]]
        });
    });




    var t = setTimeout(mudarCores, 1);

}


function mudarCores() {

    var table = document.getElementById("listaOcorrencias");


    for (var index of last10) {

        var row = table.rows[index];
        row.style.color = "red";
    }
}






//----------------------------------------------------------------------------------------------------------------
function getDate6(date) {
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
    return d + "/" + mo + "/" + a + " " + h + ":" + m + "h";
}
function checkTime(i) {
    if (i < 10) { i = "0" + i };  // add zero in front of numbers < 10
    return i;
}