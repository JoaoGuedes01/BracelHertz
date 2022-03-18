const video = document.getElementById('video');
var valido = false;
const imageUpload = document.getElementById('novaCara');
var eValido = true;
var ola = "";


Promise.all([
  faceapi.nets.tinyFaceDetector.loadFromUri('./face/models'),
  faceapi.nets.faceLandmark68Net.loadFromUri('./face/models'),
  faceapi.nets.faceRecognitionNet.loadFromUri('./face/models'),
  faceapi.nets.faceExpressionNet.loadFromUri('./face/models'),
  faceapi.nets.ssdMobilenetv1.loadFromUri('./face/models')
])



video.addEventListener('play', () => {
  const canvas = faceapi.createCanvasFromMedia(video)
  document.body.append(canvas)
  canvas.style.zIndex = "3";

  video.style.width = "" + document.getElementById("divVideo").offsetWidth + "px";
  video.style.height = "auto";

  var largura = document.getElementById("video").offsetWidth;
  var altura = document.getElementById("video").offsetHeight;

  const displaySize = { width: largura, height: altura }
  faceapi.matchDimensions(canvas, displaySize)
  setInterval(async () => {
    const detections = await faceapi.detectAllFaces(video, new faceapi.TinyFaceDetectorOptions()).withFaceLandmarks().withFaceExpressions()
    const resizedDetections = faceapi.resizeResults(detections, displaySize)
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height)
    faceapi.draw.drawDetections(canvas, resizedDetections)
    faceapi.draw.drawFaceLandmarks(canvas, resizedDetections)
    faceapi.draw.drawFaceExpressions(canvas, resizedDetections)

    if (typeof detections[0] !== 'undefined') {
      //console.log("valido");
      valido = true;
    } else {
      //console.log("invalido");
      valido = false;
    }

  }, 100)
})


//-------------------------------------------------------------------START VIDEO------------------------------------------------------

const btn = document.getElementById("btnVideo");
const divVideo = document.getElementById("divVideo");

function startVideo() {
  divVideo.style.display = "block";

  navigator.getUserMedia(
    { video: {} },
    stream => video.srcObject = stream,
    err => console.error(err)
  )
}



//-------------------------------------------------------------------END VIDEO------------------------------------------------------

function endVideo() {

  const stream = video.srcObject;
  const tracks = stream.getTracks();
  const canvas = document.getElementsByTagName("CANVAS")[1];

  tracks.forEach(function (track) {
    track.stop();
  });

  video.srcObject = null;

  divVideo.style.display = "none";
  if (typeof canvas !== 'undefined') {
    canvas.remove();
  }
}


//------------------------------------------------------------------TAKE PHOTO-----------------------------------------------------------------

document.getElementById("btnTakePhoto").addEventListener("click", () => {

  if (valido) {

    var canvas, ctx;
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
    var dataURI = canvas.toDataURL('image/jpeg');
    endVideo();
    document.getElementById("FtRecFac").src = dataURI;


    var blob = dataURLtoBlob(dataURI);
    ola = new FormData();
    ola.append("file", blob);
    ola.append("passwordInserted", "pass")

  } else {
    Swal.fire(
      'Esta foto não é válida!',
      '',
      'warning'
    )
  }


})

//-----------------------------------------------------------REGISTAR PHOTO----------------------------------------------------------------

document.getElementById("RegistarReconhecimento").addEventListener("click", () => {

  var password = document.getElementById("Pass").value.trim();
  var ft = document.getElementById("FtRecFac").src;


  if (!(password == "" || ft == "")) {
    if (eValido) {

      ola.set("passwordInserted", password);


      fetch('http://127.0.0.1:8080/api/facial-recognition/upload', {
        mode: 'cors',
        method: 'POST',
        body: ola,
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
              'Reconhecimento facial registado com sucesso!',
              '',
              'success'
            ).then(() => {
              location.reload();
            })

          }
          else {
            swal("Erro!", "Erro!", "error")
              .then(() => {
                //location.reload();
              })

            //swal({ title: `${result.value.userMessage.message.pt}` });
          }
        });




    } else {
      Swal.fire(
        'Esta foto não é válida!',
        '',
        'warning'
      )
    }
  } else {
    Swal.fire(
      'Inserir palavra-passe e verificar se a foto aparece!',
      '',
      'warning'
    )
  }


})


//----------------------------------------------dataURLtoBlob---------------------------------------------
function dataURLtoBlob(dataurl) {
  var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new Blob([u8arr], { type: mime });
}



//-----------------------------------------------------load image-----------------------------------------------------

var TesteFile = function (event) {
  var image = document.getElementById('FtRecFac');
  image.src = URL.createObjectURL(event.target.files[0]);

  ola = new FormData();
  ola.append("file", event.target.files[0]);
  ola.append("passwordInserted", "pass");
  size = ~~(event.target.files[0].size / 1024);

};

imageUpload.addEventListener('change', async () => {
  const image = await faceapi.bufferToImage(imageUpload.files[0])
  const detections = await faceapi.detectAllFaces(image).withFaceLandmarks().withFaceDescriptors()
  console.log(detections.length);
  if (detections.length != 1) {
    eValido = false;
  } else {
    eValido = true;
  }
})