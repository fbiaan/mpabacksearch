package com.distribuidora.searchengine.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/mpasearch")
public class SearchBasicController {

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

	
	public List getDemoPostgre() {
		//String sql ="SELECT x.nro_prev , x.prev_localidad , x.prev_fecha , x.prev_funcionariospol, x.relatos_hecho  "
		//		+ "FROM mpasis.prev_digital x "
		//		+ "where x.idprev_digital IN (800, 801, 802,809,955,1001,1002,1799,1800,1801,1802,2145,2154,2155,2156,2157, 88710, 48301)";
		
		String sql = "SELECT x.nro_prev , x.prev_localidad , x.prev_fecha , x.prev_funcionariospol ,  x.relatos_hecho  FROM mpasis.prev_digital x where x.idprev_digital > 15000 and x.idprev_digital < 20000";
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

	
	public List getUnExpe(String param1) {
		String sql ="select idprev_digital, nro_prev , localidad_hecho ,prev_fecha , relatos_hecho "  
				+	"from prev_digital pd WHERE idprev_digital = " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getUnExpe/{param1}")
	public Map<String, Object> getUnExpediente(@PathVariable final String param1) {
		return executeService(() -> {
			return getUnExpe(param1);
			
		});
	}

	public List getPartesExpe(String param1) {
		String sql ="select idprev_ditital_partes , idprev_digital , prev_id_tipopersona , nombre , apellido , dni , fecha_nac , celular , barrio , localidad "  
				  + "from prev_digital_partes pdp where idprev_digital = " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getPartesExpe/{param1}")
	public Map<String, Object> getPartesExpediente(@PathVariable final String param1) {
		return executeService(() -> {
			return getPartesExpe(param1);
			
		});
	}

	public List getTramiteExpe(String param1) {
		String sql ="select idprev_digital_tramites , idprev_digital , asunto , fecha , texto "   
                  + "from prev_digital_tramites pdt where idprev_digital " + param1;
		return jdbcTemplate.queryForList(sql); 
	}
	
	@GetMapping("/getTramiteExpe/{param1}")
	public Map<String, Object> getTramiteExpediente(@PathVariable final String param1) {
		return executeService(() -> {
			return getTramiteExpe(param1);
			
		});
	}


}
