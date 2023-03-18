package com.distribuidora.searchengine.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distribuidora.searchengine.controller.SearchBasicController.RestServiceExecution;
import com.distribuidora.searchengine.dto.DenunciaDato;
import com.distribuidora.searchengine.dto.ModeloX;
import com.distribuidora.searchengine.dto.ResulProce1;
import com.distribuidora.searchengine.service.DenunciaService;

@CrossOrigin("*")
@RestController
@RequestMapping("/search")
public class ProceduresMysqlController {

	public interface RestServiceExecution {
		public Object execute() throws Exception;		
	}
	
	public Map<String, Object> executeService(RestServiceExecution e) {
		Map<String, Object> res = new HashMap<>();
		try {
			Object r = e.execute();
			res.put("return", r);
		} catch (Exception e2) {
			
			res.put("result", "Error");
			res.put("error", e2.getMessage());
		}
		return res;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	@GetMapping("/fseach1/{param1}")
	public ResponseEntity<?> llamaFsearch1(@PathVariable String param1) throws SQLException {
		String dbURL = "jdbc:mysql://168.181.186.118:3306/mpasis";
        String user = "dba";
        String password = "55alfred55";
		Connection conn  = DriverManager.getConnection(dbURL, user, password);
		
		System.out.println(param1);
		
		String firsCriteria = param1.replace("+", "");
		firsCriteria = firsCriteria.replace("-", "");
		String[] textoSeparado = firsCriteria.split(" "); 
		String finalCriteria = textoSeparado[0];
	
		
		
		CallableStatement statement = conn.prepareCall("{call FMatchSearch1(?)}");
		statement.setString(1, param1);
		boolean hadResults = statement.execute();
		//ResultSet resultSet = statement.getResultSet();
		System.out.println(hadResults);
		List<ResulProce1> lstLineas = new ArrayList<>();
		Integer numPersonExpe = 0;
		Integer numRelato = 0;
		while (hadResults) {
         //   ResultSet resultSet = statement.getResultSet();
            ResultSet resultSet1 = statement.getResultSet();
        
            while (resultSet1.next()) {
            	ResulProce1 dato = new ResulProce1();
            	String conditionSwitch = resultSet1.getString("opt");
            	switch (conditionSwitch) {
	            	case  "EXPE" :
		            	//ResulProce1 dato = new ResulProce1();
		            	dato.setId(resultSet1.getInt("idmes"));
		           	 	dato.setFechaConsul(resultSet1.getString("fecha"));
		           	 	dato.setResulcamp1(resultSet1.getString("resul1"));
		
		           	 	// busco donde esta el primer criterio
		           	 	String todoResul1 = resultSet1.getString("resul1");
		           	 	int indice = todoResul1.toUpperCase().indexOf(finalCriteria.toUpperCase());
		           	 	if (indice > 20) { 
		           	 		todoResul1 = todoResul1.substring(indice - 20 , indice +20);
		           	 		} else {
		           	 			
		           	 			todoResul1 = todoResul1.substring(0);
		           	 		}
		           	 	dato.setResulLegajo(todoResul1);	
			           	 /* esto es para saber cuantas veces esta en un texto algo que se busca
			           	   while (todoResul1.indexOf(sTextoBuscado) > -1) {
			       	      sTexto = sTexto.substring(sTexto.indexOf(
			       	        sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
			       	      contador++; 
			       	    }*/
		           	 	
		           	 	// parte del uninio sde personas de legajo
		           	 	break;
	            	case "PERLEG" :
	        			numPersonExpe++;
	        			break;
	            	case "RELATO" :
	            		numRelato++;
	            		break;
            	}
            	dato.setResulPersonLegajo("Personas en Legajo : " + numPersonExpe.toString() + " , y Denuncias :" + numRelato );
           	 	lstLineas.add(dato);
            }

            hadResults = statement.getMoreResults();
        }

		statement.close();
		return new ResponseEntity<>(lstLineas, HttpStatus.OK);
	}

	
	@GetMapping("/fseach2/{param1}")
	public ResponseEntity<?> llamaFsearch2(@PathVariable String param1) throws SQLException {
		String dbURL = "jdbc:mysql://168.181.186.118:3306/mpasis";
        String user = "dba";
        String password = "55alfred55";
		Connection conn  = DriverManager.getConnection(dbURL, user, password);
		
		System.out.println(param1);
			
		CallableStatement statement = conn.prepareCall("{call FMatchSearch1(?)}");
		statement.setString(1, param1);
		boolean hadResults = statement.execute();
	
		List<Integer> listaResul = new ArrayList<>();
		HashMap<Integer, String> hasDato = new HashMap<>();
		System.out.println(hadResults);
		while (hadResults) {    
            ResultSet resultSet1 = statement.getResultSet();      
            while (resultSet1.next()) {
            	ResulProce1 dato = new ResulProce1();
            	if (resultSet1.getString("idmes") != null) {
            		listaResul.add(resultSet1.getInt("idmes"));
            		hasDato.put(resultSet1.getInt("idmes"), resultSet1.getString("resul1"));
            		System.out.println(resultSet1.getInt("idmes"));
            	//String conditionSwitch = resultSet1.getString("opt");
            	}
            }

            hadResults = statement.getMoreResults();
        }		
		statement.close();
		
		List<Integer> lstSinduplicados = listaResul.stream()
				.distinct()
				.collect(Collectors.toList());
		
		// ya tengo la lista de id sin repetir 
		// procedo a armar los campos
		List<ModeloX> lstModel = new ArrayList<>();
		lstSinduplicados.forEach((linea) -> {
			String idmesexpe = linea.toString();
			ModeloX lineaModel = new ModeloX();
			lineaModel.setCabezeraDerTit("LEGAJO");
			lineaModel.setCabezeraDerDato(linea.toString());
			lineaModel.setCabezeraIzq("Expediente");
			lineaModel.setSectoTitulo("OFicina de RAC");
			if (hasDato.get(linea).length() < 200) { 
				lineaModel.setTextoMedio((String) hasDato.get(linea));
			}else {
				lineaModel.setTextoMedio((String) hasDato.get(linea).subSequence(0, 199));
			}
			
			lineaModel.setAbajoRJ("SI");
			lineaModel.setFecha1("18/03/2023");
			lineaModel.setAbajoPreventivo("1");
			
			// completo datos de la lista con accesso a la db 
			// CAMPO PERSONAS partes de mes expe personas
			String sql ="SELECT COUNT(idmes_expedientes_personas ) \r\n"
					+ "FROM mes_expedientes_personas mep \r\n"
					+ "WHERE idmes_expedientes =" + idmesexpe;
			List<Integer> cuentaper = jdbcTemplate.queryForList(sql, Integer.class); 
			Integer valorper = (Integer) cuentaper.get(0);		
			lineaModel.setAbajoPersonLegajo(valorper.toString());
			
			// campo arituclo , - delito , ojo null
			String sql2 ="Select CONCAT(md.nombre,' ', IFNULL(md.titulo, ''))  from mes_expedientes_delitos med\r\n"
					+ "inner join mes_delitos md on med.idmes_delitos = md.idmes_delitos  \r\n"
					+ "where idmes_expedientes = " + idmesexpe;
			//List<Integer> cuentaper = jdbcTemplate.queryForList(sql, Integer.class); 
			List<String> lstDel = jdbcTemplate.queryForList(sql2,String.class);
			String delito = "-";
			for (int x=0; x < lstDel.size(); x++) {
				delito = delito + lstDel.get(x);
			}
			lineaModel.setAbajoTituloDelito(delito);
			
			lstModel.add(lineaModel);
			
		});
				
		return new ResponseEntity<>(lstModel, HttpStatus.OK);
	}
	
}
