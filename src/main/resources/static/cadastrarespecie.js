$(document).ready( () => {

    $("#especieForm").on('submit', (event) => {
        event.preventDefault();

        let especie ={
            nome:  $("#nome").val()
        }
        
        $.ajax({
            url: '/api/passaros/setEspecie',
            type: 'POST',
            dataType: 'text',
            contentType: 'application/json',
            data: JSON.stringify(especie),
            success: (response) =>{
				alert('Espécie cadastrada com sucesso! ');
                console.log(response);
                $('#especieForm')[0].reset();
            },
            error: (error) => {
                alert('Problema ao cadastrar: \n' + error.responseText);
                console.log(error);
                $('#especieForm')[0].reset();
            }
        })
    })



})