$(document).ready(function() {

	getEspecie();
	getPassaro();

	$('#filtroForm').submit((e) => {
		e.preventDefault();
		getPassaro();
	})

	$("#printListaPassaros").click(() => {
		let ids = [];
		$("#tabelaPassaros tbody tr").each(function() {
			let id = $(this).find('td:eq(0)').text();
			ids.push(id);
		})
		$.ajax({
			url: '/api/passaros/printCollectionPassaro',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(ids),
			dataType: 'json',
			success: function(response) {
				window.open(response.link, '_blank');
			},
			error: function(error) {
				console.error('Erro ao buscar as espécies:', error);
			}
		});
	})

	$("#btn_cadastrarPassaro").click(function() {
		sessionStorage.setItem('cadastrarpassaro_editmode', 'false');
		window.location.href = "cadastrarpassaro.html";
	})
})

function getEspecie() {
	$.ajax({
		url: '/api/passaros/getAllEspecie',
		type: 'GET',
		dataType: 'json',
		success: function(especies) {
			$('#filtroEspecie').empty();
			$('#filtroEspecie').append($('<option>', {
				value: "TODAS",
				text: "Todas"
			}));
			$.each(especies, function(index, especie) {
				let option = $('#filtroEspecie').append($('<option>', {
					value: especie.nome,
					text: especie.nome,
					selected: especie.nome.toUpperCase() === 'TRINCA-FERRO'
				}));

				$('#filtroEspecie').append(option);
			});
		},
		error: function(error) {
			console.error('Erro ao buscar as espécies:', error);
		}
	});
}


function getPassaro() {
	$.ajax({
		url: '/api/passaros/getAllPassaro',
		type: 'GET',
		dataType: 'json',
		success: function(passaros) {
			let tabela = $('#corpoTabelaPassaros');
			tabela.empty();
			$.each(passaros, function(index, passaro) {
				if (checkFilters(passaro)) {
					let linha = $('<tr>');
					linha.append($('<td>').css('display', 'none').text(passaro.id));
					linha.append($('<td>').text(passaro.nome));
					linha.append($('<td>').text(passaro.numeroAnilha));
					linha.append($('<td>').text(passaro.especie));
					linha.append($('<td>').text(convertDate(passaro.dataNascimento)));
					linha.append($('<td>').text(passaro.sexo == 'MACHO' ? 'M' : passaro.sexo == 'FEMEA' ? 'F' : 'D'));
					linha.append($('<td>').text(passaro.sexado == true ? 'S' : 'N'));
					let infoBtn = $('<a>').attr('href', 'fichapassaro.html').text('Info');
					infoBtn.on('click', function() {
						sessionStorage.setItem('cadastrarpassaro_passaro_id', passaro.id);
					})
					linha.append($('<td>').append(infoBtn));

					let editarBtn = $('<a>').attr('href', 'cadastrarpassaro.html').text('Editar');
					editarBtn.on('click', function() {
						sessionStorage.setItem('cadastrarpassaro_editmode', 'true');
						sessionStorage.setItem('cadastrarpassaro_passaro_id', passaro.id);
					});
					linha.append($('<td>').append(editarBtn));
					tabela.append(linha);
				}
			});
		},
		error: function(error) {
			console.error('Erro ao buscar pais:', error);
		}
	});
}

function checkFilters(passaro) {
	const filtroEspecie = $("#filtroEspecie").val();
	const filtroSexo = $("#filtroSexo").val();
	const dataNascimentoInicial = $("#dataNascimentoInicial").val();
	const dataNascimentoFinal = $("#dataNascimentoFinal").val();
	const filtroCriatorio = $("#filtroCriatorio").val();

	if (filtroEspecie && filtroEspecie != 'TODAS' && passaro.especie != filtroEspecie) {
		return false;
	}

	if (filtroSexo && filtroSexo != 'TODOS' && passaro.sexo != filtroSexo) {
		return false;
	}

	if (dataNascimentoInicial && passaro.dataNascimento < dataNascimentoInicial) {
		return false;
	}

	if (dataNascimentoFinal && passaro.dataNascimento > dataNascimentoFinal) {
		return false;
	}

	if (filtroCriatorio && passaro.criatorio && passaro.criatorio != filtroCriatorio) {
		return false;
	}

	return true;

}

function convertDate(inputFormat) {
	let date = new Date(inputFormat);
	let dd = date.getDate();
	let mm = date.getMonth() + 1;
	let yyyy = date.getFullYear();

	if (dd < 10) {
		dd = '0' + dd
	}

	if (mm < 10) {
		mm = '0' + mm
	}
	return dd + '/' + mm + '/' + yyyy;
}