$(document).ready(function() {
	checkToken();
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
			$('#nome').text(passaro.nome);
			$('#especie').text(passaro.especie);
			$('#dataNascimento').text(passaro.dataNascimento);
			$('#pai').text(passaro.pai);
			$('#mae').text(passaro.mae);
			$('#sexo').text(passaro.sexo);
			$('#numeroAnilha').text(passaro.numeroAnilha);
			$('#sexado').prop('checked', passaro.sexado);
			$('#numeroMarcacao').text(passaro.numeroMarcacao);				
		},
		error: (error) =>{
			console.log(error);
		}
	});
	
	$.ajax({
		url: '/api/passaros/getArvoreGenealogica',
		type: 'GET',
		data: {
			id: passaroId
		},
		dataType: 'json',
		success: (arvore) =>{
			console.log(arvore);
			$('#genpai').html("Pai: <b>" + (arvore.pai != null ? arvore.pai : "( ? )")  + "</b>");
			$('#genmae').html("Mãe: <b>" + (arvore.mae != null ? arvore.mae : "( ? )")  + "</b>");
			$('#nonoPai').html("Avô Paterno: <b>" + (arvore.nonoPai != null ? arvore.nonoPai : "( ? )")  + "</b>");
			$('#nonaPai').html("Avó Paterna: <b>" + (arvore.nonaPai != null ? arvore.nonaPai : "( ? )")  + "</b>");
			$('#nonoMae').html("Avô Materno: <b>" + (arvore.nonoMae != null ? arvore.nonoMae : "( ? )")  + "</b>");
			$('#nonaMae').html("Avó Materna: <b>" + (arvore.nonaMae != null ? arvore.nonaMae : "( ? )")  + "</b>");
			$('#bisnonoNonoPai').html("Bisavô Avô Paterno: <b>" + (arvore.bisnonoNonoPai != null ? arvore.bisnonoNonoPai : "( ? )")  + "</b>");
			$('#bisnonoNonaPai').html("Bisavô Avó Paterna: <b>" + (arvore.bisnonoNonaPai != null ? arvore.bisnonoNonaPai : "( ? )")  + "</b>");
			$('#bisnonaNonoPai').html("Bisavó Avô Paterno: <b>" + (arvore.bisnonaNonoPai != null ? arvore.bisnonaNonoPai : "( ? )")  + "</b>");
			$('#bisnonaNonaPai').html("Bisavó Avó Paterna: <b>" + (arvore.bisnonaNonaPai != null ? arvore.bisnonaNonaPai : "( ? )")  + "</b>");
			$('#bisnonoNonoMae').html("Bisavô Avô Materno: <b>" + (arvore.bisnonoNonoMae != null ? arvore.bisnonoNonoMae : "( ? )")  + "</b>");
			$('#bisnonoNonaMae').html("Bisavô Avó Materna: <b>" + (arvore.bisnonoNonaMae != null ? arvore.bisnonoNonaMae : "( ? )")  + "</b>");
			$('#bisnonaNonoMae').html("Bisavó Avô Materno: <b>" + (arvore.bisnonaNonoMae != null ? arvore.bisnonaNonoMae : "( ? )")  + "</b>");
			$('#bisnonaNonaMae').html("Bisavó Avó Materna: <b>" + (arvore.bisnonaNonaMae != null ? arvore.bisnonaNonaMae : "( ? )")  + "</b>");


		},
		error: (error) =>{
			console.log(error);
		}
	});
	
	
	
	
	
})
