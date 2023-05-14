package com.distribuidora.searchengine.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.distribuidora.searchengine.controller.SearchBasicController.RestServiceExecution;
import com.distribuidora.searchengine.dto.DenunciaDato;
import com.distribuidora.searchengine.dto.ModelPdf1;
import com.distribuidora.searchengine.dto.ModeloX;
import com.distribuidora.searchengine.dto.Pasavar;
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

	@Autowired
	private DenunciaService denunciaService;
	
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

	@GetMapping("/fseach442/{param1}")
	public String soloParamprueba442(@PathVariable String param1)  {
		String dbURL = "jdbc:mysql://168.181.186.118:3306/mpasis";
        String user = "dba";
        String password = "55alfred55";
		
		
		//  analisis de param1 
		System.out.println(param1);
		
		// trabjamos el llamado del front 
		// ejemlo +%22actuaciones%20informativas%22%20fernando%20ariel%20palacios

		int primercomilla = param1.indexOf('"');
		String criteriocomilla = "";
		String criterioFront= "";
		String comparaB = "!";
		if (param1.substring(0,1).equals(comparaB)) {
			String criterioNro = '"' + param1.substring(1, param1.length()) + '"';
			System.out.println("final al api :" + criterioNro);
			return (criterioNro);
		}
		
		if (primercomilla + 1 > 0 ) {
			criteriocomilla = param1.substring(primercomilla + 1, param1.length());
			int segcomilla = criteriocomilla.indexOf('"');
			criteriocomilla = param1.substring(primercomilla , segcomilla + 2);
			System.out.println(criteriocomilla);
		
			
			if (segcomilla < param1.length()) {
				criterioFront  = param1.substring(segcomilla + 2,param1.length());
				//criterioFront  = "+" + criterioFront.replace("  ", " ");
				System.out.println(criterioFront);
				criterioFront  = criterioFront.replace(" ", " +");
				System.out.println(criterioFront);
			}
		}else
		{
			criterioFront  = param1;
			criterioFront  = criterioFront.replace(" ", " +");
		}
		String finalCriteria = "+" + (criteriocomilla.trim()) + criterioFront;
		System.out.println("final al api :" + finalCriteria);
		
		
		return (finalCriteria);
	}
	

	
	@GetMapping("/fseach2/{param1}")
	public ResponseEntity<?> llamaFsearch2(@PathVariable String param1,
			@RequestParam(value="tipobusqueda") String tipobusqueda) throws SQLException, FileNotFoundException, IOException {
		
		List<ModeloX> lstModel = new ArrayList<>();	
		String criterioFront  = soloParamprueba442(param1);
	if (tipobusqueda.equals("db") || tipobusqueda.equals("dbpdf")) {	
		
		String dbURL = "jdbc:mysql://168.181.186.118:3306/mpasis";
        String user = "dba";
        String password = "55alfred55";
		Connection conn  = DriverManager.getConnection(dbURL, user, password);
		
		//  analisis de param1 
		System.out.println(param1);
		
		/*
		Properties p = new Properties();
		p.load(new FileReader("files/config.properties"));
		p.setProperty("valorparam", param1);
		p.store(new FileWriter("files/config.properties"), "ultima busqueda");
		// trabjamos el llamado del front 
		// ejemlo +%22actuaciones%20informativas%22%20fernando%20ariel%20palacios
		*/
		
		//String criterioFront  = soloParamprueba442(param1);
		System.out.println(criterioFront);
		//
		String firsCriteria = param1.replace("+", "");
		firsCriteria = firsCriteria.replace("-", "");
		String[] textoSeparado = firsCriteria.split(" "); 
		String finalCriteria = textoSeparado[0];
		System.out.println(finalCriteria);
		
		//param1 = param1 + ",exp=1&den=1&par=1,desde=2013-05-01&hasta=2024-01-01)";
		String param2 = "exp=1&den=1&par=1";
		String param3 = "desde=2013-05-01&hasta=2024-01-01";
		CallableStatement statement = conn.prepareCall("{call FMatchSearchParam(?,?,?)}");
		statement.setString(1, criterioFront);
		statement.setString(2, param2);
		statement.setString(3, param3);
		boolean hadResults = statement.execute();
		
		
		
		List<Integer> listaResul = new ArrayList<>();
		HashMap<Integer, String> hasDato = new HashMap<>();
		HashMap<Integer, String> hasFecha = new HashMap<>();
		HashMap<Integer, String> hasOpt = new HashMap<>();
		System.out.println(hadResults);
		while (hadResults) {    
            ResultSet resultSet1 = statement.getResultSet();      
            while (resultSet1.next()) {
            	ResulProce1 dato = new ResulProce1();
            	if (resultSet1.getString("idmes") != null) {
            		listaResul.add(resultSet1.getInt("idmes"));
            		hasDato.put(resultSet1.getInt("idmes"), resultSet1.getString("resul1"));
            		hasFecha.put(resultSet1.getInt("idmes"), resultSet1.getString("fecha"));
            		hasOpt.put(resultSet1.getInt("idmes"), resultSet1.getString("opt"));
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
		//List<ModeloX> lstModel = new ArrayList<>();
		System.out.println("la lista tiene : " + lstSinduplicados.size());
		lstSinduplicados.forEach((linea) -> {
			String idmesexpe = linea.toString();
			//System.out.println("en bucle para datos id mes expe : " + idmesexpe);
			String sqlNro = "select nro_exp  from mes_expedientes me where idmes_expedientes = " + idmesexpe;
			
			try {
				String nroExpe = jdbcTemplate.queryForObject(sqlNro, String.class);
				
				
				
				ModeloX lineaModel = new ModeloX();
				lineaModel.setIdexpelong(idmesexpe);
				lineaModel.setCabezeraDerTit("LEGAJO");
				lineaModel.setCabezeraDerDato(nroExpe + "(" + idmesexpe + ")");
				lineaModel.setCabezeraIzq(hasOpt.get(linea));
				
				
				//  <<<  titulo medio azul  lugar lugar
	//			String sqlTit = "SELECT cs.label from mes_expedientes me \r\n"
	//					+ "INNER JOIN mes_expedientes_movimientos_detalles memd ON me.idmes_expedientes = memd.idmes_expedientes \r\n"
	//					+ "inner join mes_expedientes_movimientos mem ON mem.idmes_expedientes_movimientos = memd.idmes_expedientes_movimientos \r\n"
	//					+ "inner join cfg_sectores cs on cs.id = memd.id_sector_anterior \r\n"
	//					+ "where me.idmes_expedientes = " + idmesexpe + " limit 1";
				
				String sqlTit = "SELECT  cs.label  from mes_expedientes me\r\n"
						+ "inner join cfg_sectores cs on cs.id = me.idmes_fiscalia_turno \r\n"
						+ "where idmes_expedientes = " + idmesexpe + " limit 1";
				String titulo = "";
				try {
				 titulo = jdbcTemplate.queryForObject(sqlTit, String.class);
				 } catch (DataAccessException ex) {
					 titulo  = "NO ENCUENTRA SECTOR - sin registros en DB";
				 }
				
				lineaModel.setSectoTitulo(titulo);
				
				//texto medio 
				// busco donde esta el primer criterio  variable  finalCriteria
	       	 	String todoResul1 = hasDato.get(linea);
				if (todoResul1.length() < 200) { 
					//lineaModel.setTextoMedio((String) hasDato.get(linea));
				}else {
					int indice = todoResul1.toUpperCase().indexOf(finalCriteria.toUpperCase());
	
					if (todoResul1.length() < 300) {
						if (indice > 200) {
							todoResul1 = todoResul1.substring(indice - 100, todoResul1.length());
						}else if (indice < 100) {
							todoResul1 = todoResul1.substring(0 , indice +100);
						}else {
							todoResul1 = todoResul1.substring(indice - 100 , todoResul1.length());
						}
					}else {
						if (indice < 100) {
							todoResul1 = todoResul1.substring(0 , indice +100);	
						} else if ((indice + 100) < todoResul1.length()) {
							todoResul1 = todoResul1.substring(indice - 100 , indice +100);
						} else {
							todoResul1 = todoResul1.substring(indice - 100 , todoResul1.length());
						}
					}		
				}
				lineaModel.setTextoMedio(todoResul1);
				
				// otros arametro remitido a juicio
				lineaModel.setAbajoRJ("SI");
				lineaModel.setFecha1(hasFecha.get(linea));
				lineaModel.setAbajoPreventivo("1");
				
				// completo datos de la lista con accesso a la db 
				// CAMPO PERSONAS partes de mes expe personas
				String sql ="SELECT COUNT(idmes_expedientes_personas) \r\n"
						+ "FROM mes_expedientes_personas mep \r\n"
						+ "WHERE idmes_expedientes =" + idmesexpe;
				List<Integer> cuentaper = jdbcTemplate.queryForList(sql, Integer.class); 
				Integer valorper = (Integer) cuentaper.get(0);		
				lineaModel.setAbajoPersonLegajo(valorper.toString());
				
				// campo arituclo , - delito , ojo null
				String sql2 ="Select DISTINCT CONCAT(md.nombre,' ', IFNULL(md.titulo, ''))  from mes_expedientes_delitos med\r\n"
						+ "inner join mes_delitos md on med.idmes_delitos = md.idmes_delitos  \r\n"
						+ "where idmes_expedientes = " + idmesexpe;
				//List<Integer> cuentaper = jdbcTemplate.queryForList(sql, Integer.class); 
				List<String> lstDel = jdbcTemplate.queryForList(sql2,String.class);
				String delito = "-";
				for (int x=0; x < lstDel.size(); x++) {
					delito = delito + " / " + lstDel.get(x);
				}
				if (delito.length()< 100) {
					lineaModel.setAbajoTituloDelito(delito);
				}else {
					lineaModel.setAbajoTituloDelito(delito.substring(0, 100));
				}
				
				/// >>>>>>>>>>>>>>>>>>>>>  SECUENTROS <zzecuentros   -.-.-.-.-.-.-.-.-
				
				String sql3 ="select COUNT(idmes_expedientes) from mes_expedientes_secuestro_automotor mesa \r\n"
						+ "WHERE idmes_expedientes = " + idmesexpe + "\r\n"
						+ "union all\r\n"
						+ "select COUNT(idmes_expedientes) from mes_expedientes_secuestro_otro meso \r\n"
						+ "WHERE idmes_expedientes = " + idmesexpe + "\r\n"
						+ "union all \r\n"
						+ "select COUNT(idmes_expedientes) from mes_expedientes_secuestro_dinero mesd  \r\n"
						+ "WHERE idmes_expedientes = " + idmesexpe + "\r\n"
						+ "union all \r\n"
						+ "select COUNT(idmes_expedientes) from mes_expedientes_secuestro_arma mesa2   \r\n"
						+ "WHERE idmes_expedientes = " + idmesexpe;
				
				List<Integer> cuentadelitos = jdbcTemplate.queryForList(sql3, Integer.class);
				Integer sumade =  cuentadelitos.stream().mapToInt(Integer::intValue).sum();
				lineaModel.setAbajoSecuestros(sumade.toString());
				
				//  <<<<<<<<<<<<<< fin seucuetros <<<<<<<<<<<<<<
				
				
				lstModel.add(lineaModel);
			} // del try , si encontro el id mes expe
		catch (DataAccessException e) {
			System.out.println("se hace referencia a un id mes expediente que no existe " + idmesexpe);
		}
		});
	} // del if de tipo busqueda	
	
	if (tipobusqueda.equals("pdf") || tipobusqueda.equals("dbpdf")) {
		//AGREGO PDF 
		// saco los mas  tiene que ir vacio
		String criteriopdf  = criterioFront.replace("+", "");
		//List<ModelPdf1> lstpdf = denunciaService.todospdf(criteriopdf);
		System.out.println("criterio antes de llamar al service :" + criteriopdf);
		List<ModelPdf1> lstpdf = denunciaService.todospdflinux(criteriopdf);
		System.out.println(" lista del service " + lstpdf.size()); 
		for (int i=0; i < lstpdf.size() ; i++  ){
			ModeloX lineaModel = new ModeloX();
			lineaModel.setCabezeraIzq(lstpdf.get(i).getNombreArchivo() );
			lineaModel.setSectoTitulo(lstpdf.get(i).getPathArchivo());
			lineaModel.setTextoMedio(lstpdf.get(i).getRespuesta());
			lineaModel.setCabezeraDerTit("PDF");
			lineaModel.setFecha1("__");
			lineaModel.setAbajoTituloDelito(lstpdf.get(i).getPathlinux());
			lstModel.add(lineaModel);
		}	
		} // del iff de tipo busqueda pdf
		return new ResponseEntity<>(lstModel, HttpStatus.OK);
	}

	private void setCabezeraDerDato(String idmesexpe) {
		// TODO Auto-generated method stub
		
	}
	
}
