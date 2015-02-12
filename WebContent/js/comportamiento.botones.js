function randomNetworkSelected(){
        document.getElementById("numNodes").disabled = false;
        document.getElementById("nameRandom").disabled = false;
        document.getElementById("datasetIdentifier").disabled = true;
        document.getElementById("loadButton").disabled = true;
        document.getElementById("createButton").disabled = true;
      }

      function loadNetworkSelected(){
        document.getElementById("numNodes").disabled = true;
        document.getElementById("nameRandom").disabled = true;
        document.getElementById("datasetIdentifier").disabled = false;
        document.getElementById("loadButton").disabled = false;
        document.getElementById("createButton").disabled = false;

      }

      function createNetwork(){
        window.open('http://localhost:7474/browser','_blank');
      }
  
      function loadNetwork(){
        if(document.randomForm.bbddId.value == ""){
          alert("Please introduce a valid bbdd identifier");
        }else{
          document.randomForm.loadPushed.value="ok";
        }
      }