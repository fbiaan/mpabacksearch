package com.distribuidora.searchengine.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distribuidora.searchengine.dto.DenunciaDato;
import com.distribuidora.searchengine.dto.Denunciagrilla;
import com.distribuidora.searchengine.dto.Resugrilla;
import com.distribuidora.searchengine.repository.DenunciaRepository;
import com.distribuidora.searchengine.service.DenunciaService;

@CrossOrigin("*")
@RestController
@RequestMapping("/mpasearch")
public class SearchBasicController {

	public interface RestServiceExecution {

		public Object execute() throws Exception;
		
	}
	
	
	
	@Autowired
	DenunciaService denunciaService;
	
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

	
	public List getDemoPostgre() {
		//String sql ="SELECT x.nro_prev , x.prev_localidad , x.prev_fecha , x.prev_funcionariospol, x.relatos_hecho  "
		//		+ "FROM mpasis.prev_digital x "
		//		+ "where x.idprev_digital IN (800, 801, 802,809,955,1001,1002,1799,1800,1801,1802,2145,2154,2155,2156,2157, 88710, 48301)";
		
		//String sql = "SELECT x.nro_prev , x.prev_localidad , x.prev_fecha , x.prev_funcionariospol ,  x.relatos_hecho  FROM mpasis.prev_digital x where x.idprev_digital > 15000 and x.idprev_digital < 20000";
		String sql = "SELECT x.nro_prev , x.prev_localidad , x.prev_fecha , x.prev_funcionariospol ,  x.relatos_hecho  FROM mpasis.prev_digital x where x.idprev_digital > 105000 and x.idprev_digital < 106000";
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/searchdemo")
	public Map<String, Object> getDemoPostgre2() {
		return executeService(() -> {
			return getDemoPostgre();
			
		});
	}
	
	String parteSelect = "select  x.nro_prev , x.prev_fecha, x.localidad_hecho ,x.prev_funcionariospol , x.relatos_hecho "
							+ "from mpasis.prev_digital x "
							+ "where ";
	
	String parteWhere = "concat_ws(' ', prev_localidad,calle_hecho,barrio_hecho,localidad_hecho, referencias_hecho,relatos_hecho)  like '%";  //+ param1 + "%' \r\n";
	String parteAnd =  "AND concat_ws(' ', prev_localidad,calle_hecho,barrio_hecho,localidad_hecho, referencias_hecho,relatos_hecho)  like '%";
	
	public List getConcatWs(String param1) {
		String sql = parteSelect + parteWhere + param1 + "%'"; 
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/searchConcat/{param1}")
	public Map<String, Object> getConcatWsCommon(
			@PathVariable final String param1) {
		return executeService(() -> {
			return getConcatWs(param1);		
		});
	}
	
	public List getConcatWs2(String param1, String param2) {
		String sql = parteSelect + parteWhere + param1 + "%' " + parteAnd + param2 + "%' ";   
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/searchConcat2/{param1}/{param2}")
	public Map<String, Object> getConcatWsCommon2(
			@PathVariable final String param1, @PathVariable final String param2) {
		return executeService(() -> {
			return getConcatWs2(param1, param2);		
		});
	}
		
	public List getConcatWs3(String param1, String param2, String param3) {
		String sql = parteSelect + parteWhere + param1 + "%' " + parteAnd + param2 + "%' "  + parteAnd + param3 + "%' ";   
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/searchConcat3/{param1}/{param2}/{param3}")
	public Map<String, Object> getConcatWsCommon3(
			@PathVariable final String param1, @PathVariable final String param2, @PathVariable final String param3) {
		return executeService(() -> {
			return getConcatWs3(param1, param2, param3);		
		});
	}

	public List getConcatWs4(String param1, String param2, String param3, String param4) {
		String sql = parteSelect + parteWhere + param1 + "%' " + parteAnd + param2 + "%' "  + parteAnd + param3 + "%' "  + parteAnd + param4 + "%' ";   
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/searchConcat4/{param1}/{param2}/{param3}/{param4}")
	public Map<String, Object> getConcatWsCommon3(
			@PathVariable final String param1, @PathVariable final String param2, @PathVariable final String param3, @PathVariable final String param4) {
		return executeService(() -> {
			return getConcatWs4(param1, param2, param3, param4);		
		});
	}

	
	public List getUnDenun(String param1) {
		String sql ="select idprev_digital, nro_prev , localidad_hecho ,prev_fecha , relatos_hecho "  
				+	"from prev_digital pd WHERE idprev_digital = " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getUnaDenuncia/{param1}")
	public Map<String, Object> getUnaDenuncia(@PathVariable final String param1) {
		return executeService(() -> {
			return getUnDenun(param1);
			
		});
	}


	
	public List getPartesDenun(String param1) {
		String sql ="select idprev_ditital_partes , idprev_digital , prev_id_tipopersona , nombre , apellido , dni , fecha_nac , celular , barrio , localidad "  
				  + "from prev_digital_partes pdp where idprev_digital = " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getPartesDenuncia/{param1}")
	public Map<String, Object> getPartesDenuncia(@PathVariable final String param1) {
		return executeService(() -> {
			return getPartesDenun(param1);
			
		});
	}


	// esto falta invocar desde el front ,  completar 
	public List getTramiteExpe(String param1) {
		String sql ="select idprev_digital_tramites , idprev_digital , asunto , fecha , texto "   
                  + "from prev_digital_tramites pdt where idprev_digital = " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getTramiteExpe/{param1}")
	public Map<String, Object> getTramiteExpediente(@PathVariable final String param1) {
		return executeService(() -> {
			return getTramiteExpe(param1);
			
		});
	}

	public List getMesSecc(String param1) {
		String sql ="SELECT pd.idprev_digital , pd.nro_prev , pd.fecha_hecho, pd.localidad_hecho\r\n"
				+ "		   , me.caratula, ms.nombre , ms.regional  \r\n"
				+ "	FROM prev_digital pd INNER JOIN mes_expedientes AS me\r\n"
				+ "		ON pd.idprev_digital = me.idprev_digital \r\n"
				+ "		INNER JOIN mes_seccionales ms ON me.idmes_seccionales = ms.idmes_seccionales \r\n"
				+ "		WHERE pd.idprev_digital = " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getmesexpe/{param1}")
	public Map<String, Object> getMesSecc1(@PathVariable final String param1) {
		return executeService(() -> {
			return getMesSecc(param1);
			
		});
	}
	
	
	
	@GetMapping("/llamaproce")
	public ResponseEntity<List<Denunciagrilla>> lista(){
		List<Denunciagrilla> lista = denunciaService.lista();
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}
	

	@GetMapping("/llamaproce1/{param1}")
	public ResponseEntity<?> lista3(@PathVariable String param1) throws SQLException{
		//List<Resugrilla> lista = denunciaService.listanew();
		String dbURL = "jdbc:mysql://168.181.186.118:3306/mpasis";
        String user = "dba";
        String password = "55alfred55";
		Connection conn  = DriverManager.getConnection(dbURL, user, password);
		
		CallableStatement statement = conn.prepareCall("{call pruebaSearch3(?)}");
		statement.setString(1, param1);
		boolean hadResults = statement.execute();
		//ResultSet resultSet = statement.getResultSet();
		System.out.println(hadResults);
		List<DenunciaDato> lstdato = new ArrayList<>();
		while (hadResults) {
             ResultSet resultSet = statement.getResultSet();

             // process result set
             int i = 1 ;

             while (resultSet.next()) {
            	 DenunciaDato dato = new DenunciaDato();
            	 dato.setId(resultSet.getInt("idprev_digital"));
            	 dato.setNroprev(resultSet.getInt("nro_prev"));
            	 dato.setFechahecho(resultSet.getString("fecha_hecho"));
            	 dato.setDato(resultSet.getString("dato"));
            	 lstdato.add(dato);
            	 
                 //String title = resultSet.getString("title");
//                 String description = resultSet.getString("description");
//                 int rating = resultSet.getInt("rating");
         			//System.out.println( i + " " + resultSet.getString("dato" ));
         			i++;
//                 System.out.println(
//                         "| " + title + " | " + description + " | " + rating + " |");
             }

             hadResults = statement.getMoreResults();
         }

		statement.close();
		return new ResponseEntity<>(lstdato, HttpStatus.OK);
	}

	
	// <<<<<<<<<<<<<<<<<<<<           parte 2 de EXPEDIENTE REAL <<<<<<<<<<<<<<<
	//-----------------------------------------------------------------
	
	@GetMapping("/getUnExpediente/{param1}")
	public ResponseEntity<?> getUnExpe(@PathVariable final String param1) throws FileNotFoundException, IOException {
		String sql ="select idmes_expedientes , nro_exp , fecha_ingreso , caratula  from mes_expedientes me \r\n"
				+ "where idmes_expedientes = " + param1;
		
		//Properties p = new Properties();
		//p.load(new FileReader("files/config.properties"));
		
		//System.out.println("uno="+p.getProperty("valorparam"));
		
		return new ResponseEntity<>(jdbcTemplate.queryForList(sql),HttpStatus.OK);		
	}
	
	@GetMapping("/getpartesExpe/{param1}")
	public ResponseEntity<?> getPartesExpe(@PathVariable final String param1) {
		String sql ="select mep.idmes_expedientes_personas idper , mpc.nombre tipo , mep.nombre, mep.apellido , mep.dni  from mes_expedientes_personas mep \r\n"
				+ "INNER JOIN mes_personas_caracter mpc ON mep.idmes_personas_caracter = mpc.idmes_personas_caracter \r\n"
				+ "where idmes_expedientes =" + param1;
		return new ResponseEntity<>(jdbcTemplate.queryForList(sql),HttpStatus.OK);	
	}
	
	@GetMapping("/getexpecondenun/{param1}")
	public ResponseEntity<?> getExpeconDenuncia(@PathVariable final String param1) {
		String sql ="select idprev_digital , idmes_expedientes , prev_localidad , expediente_seccional , \r\n"
				+ "relatos_hecho  from prev_digital pd \r\n"
				+ "where idmes_expedientes =" + param1;
		return new ResponseEntity<>(jdbcTemplate.queryForList(sql),HttpStatus.OK);	
	}
	
	
	@GetMapping("/getsecuestros/{param1}")
	public ResponseEntity<?> getSecuestros(@PathVariable final String param1) {
		String sql ="select (FLOOR(RAND()* 10000)) as idsec, CONCAT('Automotor : ', mesa.marca, ' ' , mesa.modelo, mesa.dominio) as secu from mes_expedientes_secuestro_automotor mesa \r\n"
				+ "WHERE idmes_expedientes = " + param1 +  "\r\n"
				+ "union all\r\n"
				+ "select (FLOOR(RAND()* 10000)) as idsec, CONCAT('Otros : ' , meso.detalle, ' ' ,meso.fecha, IFNULL(meso.evidencia,' sin evidencia.')) from mes_expedientes_secuestro_otro meso \r\n"
				+ "WHERE idmes_expedientes = " + param1 +  "\r\n"
				+ "union all \r\n"
				+ "select (FLOOR(RAND()* 10000)) as idsec, CONCAT('Dinero : ', mesd.tipo , ' ' ,mesd.cantidad , mesd.detalle)  from mes_expedientes_secuestro_dinero mesd  \r\n"
				+ "WHERE idmes_expedientes = " + param1 +  "\r\n"
				+ "union all \r\n"
				+ "select (FLOOR(RAND()* 10000)) as idsec, CONCAT('Arma : ', mesa2.tipo, ' ' ,mesa2.marca, mesa2.calibre,mesa2.detalle) from mes_expedientes_secuestro_arma mesa2   \r\n"
				+ "WHERE idmes_expedientes = " + param1;
		
		return new ResponseEntity<>(jdbcTemplate.queryForList(sql),HttpStatus.OK);	
	}
	
	@GetMapping("/criteriobus")
	public String getCriterio() throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileReader("files/config.properties"));
		
		System.out.println("uno="+p.getProperty("valorparam"));
		return p.getProperty("valorparam");
	}
	
}
