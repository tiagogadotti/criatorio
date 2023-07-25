$(document).ready(function() {
    getEspecie();
    let editMode = sessionStorage.getItem('cadastrarpassaro_editmode');
    editMode = editMode === 'true';
    
    if(editMode){
		let passaroId = sessionStorage.getItem('cadastrarpassaro_passaro_id');
		passaroId = Number.parseInt(passaroId);
		
		$.ajax({
			url: '/api/passaros/getPassaroById',
			type: 'GET',
			data: {
				id: passaroId
			},
			dataType: 'json',
			success: (passaro) =>{
				$('#nome').val(passaro.nome);
				$('#especie').val(passaro.especie);
				$('#dataNascimento').val(passaro.dataNascimento);
				setPai(passaro.especie).done(function(){
					$('#pai').val(passaro.pai);
				});
    			setMae(passaro.especie).done(function(){
					$('#mae').val(passaro.mae);
				});
				$('#sexo').val(passaro.sexo);
				$('#numeroAnilha').val(passaro.numeroAnilha);
				$('#sexado').prop('checked', passaro.sexado);
				$('#numeroMarcacao').val(passaro.numeroMarcacao);
				
				console.log('criatorio ' +passaro.criatorio);
				$('#criatorio').val(passaro.criatorio);		
			},
			error: (error) =>{
				console.log(error);
			}
		})		
	}
       
    $("#especie").change(() =>{
		const especie = $("#especie").val();
		setPai(especie);
    	setMae(especie);
	});
    
    

    $('#passaroForm').on('submit', function(event) {
        event.preventDefault();
		let passaroId = sessionStorage.getItem('cadastrarpassaro_passaro_id');
        var passaro = {
			id: passaroId,
            nome: $('#nome').val(),
            especie: $('#especie').val(),
            dataNascimento: $('#dataNascimento').val(),
            sexo: $('#sexo').val(),
            numeroAnilha: $('#numeroAnilha').val(),
            sexado: $('#sexado').is(':checked'),
            numeroMarcacao: $('#numeroMarcacao').val(),
            pai: $('#pai').val(),
            mae: $('#mae').val(),
            criatorio: $('#criatorio').val()
        };

        $.ajax({
            url: '/api/passaros/setPassaro',
            type: 'POST',
            dataType: 'text',
            contentType: 'application/json',
            data: JSON.stringify(passaro),
            success: function(response) {
                alert('Pássaro salvo com sucesso');
                $('#passaroForm')[0].reset();
            },
            error: function(error) {
                console.error('Erro ao salvar o pássaro:', error);
            }
        });
    });
});


function getEspecie(){
	 $.ajax({
        url: '/api/passaros/getAllEspecie',
        type: 'GET',
        dataType: 'json',
        success: function(especies) {
			console.log(JSON.stringify(especies));
			
			$('#especie').empty();
            $('#especie').append($('<option>', {
                value: "TODAS",
                text: "Selecione uma espécie"
            }));
			
            $.each(especies, function(index, especie) {
                $('#especie').append($('<option>', {
                    value: especie.nome,
                    text: especie.nome
                }));
            });
        },
        error: function(error) {
            console.error('Erro ao buscar as espécies:', error);
        }
    });
}

function setPai(especie){
	return $.ajax({
        url: '/api/passaros/getAllPassaroBySexoAndEspecieNome',
        type: 'GET',
        data:{
			sexo: 'MACHO',
			especie_nome: especie
		},
        dataType: 'json',
        success: function(passaros) {
			$('#pai').empty();
            $('#pai').append($('<option>', {
                    value: 'Todos',
                    text: 'Selecione'
                }));
            $.each(passaros, function(index, passaro) {
                var option = $('<option>', {
			        value: passaro.nome,
			        text: passaro.nome
			    });
			    if(passaro.criatorio == 'BOA'){
			        option.css('font-weight','bold');
			    }
			    $('#pai').append(option);
            });
        },
        error: function(error) {
			$('#pai').empty();
			$('#pai').append($('<option>', {
                    value: 'Todos',
                    text: 'Selecione'
                }));
            console.error('Erro ao buscar pais:', error);
        }
    });
}

function setMae(especie){
	return $.ajax({
        url: '/api/passaros/getAllPassaroBySexoAndEspecieNome',
        type: 'GET',
        data:{
			sexo: 'FEMEA',
			especie_nome: especie
		},
        dataType: 'json',
        success: function(passaros) {
            $('#mae').empty();
            $('#mae').append($('<option>', {
                    value: 'Todas',
                    text: 'Selecione'
                }));
            $.each(passaros, function(index, passaro) {
			    var option = $('<option>', {
			        value: passaro.nome,
			        text: passaro.nome
			    });
			    if(passaro.criatorio == 'BOA'){
			        option.css('font-weight','bold');
			    }
			    $('#mae').append(option);
			});
        },
        error: function(error) {
			$('#mae').empty();
			$('#mae').append($('<option>', {
                    value: 'Todas',
                    text: 'Selecione'
                }));
            console.error('Erro ao buscar mães:', error);
        }
    });
}
