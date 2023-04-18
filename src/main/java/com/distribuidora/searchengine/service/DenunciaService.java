package com.distribuidora.searchengine.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.distribuidora.searchengine.dto.Denunciagrilla;
import com.distribuidora.searchengine.dto.ModelPdf1;
import com.distribuidora.searchengine.dto.Resugrilla;
import com.distribuidora.searchengine.repository.DenunciaRepository;

@Service
public class DenunciaService {
	@Autowired
	DenunciaRepository denunciaRepository;
	
	public List<Denunciagrilla> lista(){
		return denunciaRepository.llamaproce();
	}
	
	public List<Resugrilla> listanew(){
		return denunciaRepository.llamaprocedos();
	}
	
	
	public String leerpdf1( String criterio) {
		PDDocument pdDocument = null;
		try {
		   // pdDocument = PDDocument.load(new File("C:/intel/pdf/Organizacion del Equipo de trabajo.pdf"));
		    pdDocument = PDDocument.load(new File("C:/intel/pdf/Preventivo Digital.pdf"));
		    PDFTextStripper pdfStripper = new PDFTextStripper();
		    pdfStripper.setStartPage(1);
		    pdfStripper.setEndPage(5);
		    String parsedText = pdfStripper.getText(pdDocument);
		    Integer intIndex = parsedText.toUpperCase().indexOf(criterio.toUpperCase());
		    String buscar= "";
		    if (intIndex != - 1) {
		    	buscar = "Palabra encontrada , posicion " + intIndex.toString();
		    }else {
		    	buscar ="Palabra NO encontrada";
		    	
		    }
		    System.out.println(parsedText + "/r/n" + buscar);
		    //return parsedText + "/r/n" + buscar;
		    return buscar;
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} finally {
		    if (pdDocument != null) {
		        try {
		            pdDocument.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
		return null;
		
	}
	
	
	public List<ModelPdf1> todospdf (String criterio) {
		List<String> losPath = new ArrayList<>();
		losPath.add("C:\\pdf\\includes\\archivos_preventivo");
		losPath.add("C:\\pdf\\includes\\archivos_firmados");
		losPath.add("C:\\pdf\\includes\\scan_expedientes");
		losPath.add("C:\\pdf\\sistemaArchivos\\cavArchivos");
		losPath.add("C:\\pdf\\sistemaArchivos\\racDocumentacion");
		losPath.add("C:\\pdf\\sne\\escritosAdjuntos");
		losPath.add("C:\\pdf\\sne\\escritosPDF");
		List<ModelPdf1> lstrespu = new ArrayList<ModelPdf1>();
		int tot = 0;
		for (int i=0; i<losPath.size(); i++) {
					//File dir = new File("c:\\intel\\pdf");
			String mipath = losPath.get(i);
			File dir = new File(mipath);
			String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
			
	        if (ficheros == null)//EXCEPCION
	              System.out.println("No hay archivos en la carpeta especificada");
	        else {
	            for (int x=0;x<ficheros.length;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
	                String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
	                //ruta=("c:\\intel\\pdf\\"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
	                ruta=(mipath + "\\"+ficheros[x]);
	                //System.out.println(tot + " - " + x + " .- " + ruta);
	                tot++;
	                  try {
	                	ModelPdf1 respulinea = new ModelPdf1();
	                	PDDocument pdDocument = null;
	                	pdDocument = PDDocument.load(new File(ruta)); //CARGAR EL PDF
	                     
	                     // List l = pd.getDocumentCatalog().getAllPages();//NUMERO LAS PAGINAS DEL ARCHIVO
	          		    PDFTextStripper pdfStripper = new PDFTextStripper();
	        		    pdfStripper.setStartPage(1);
	        		    pdfStripper.setEndPage(5);
	        		    String parsedText = pdfStripper.getText(pdDocument);
	        		    Integer intIndex = parsedText.toUpperCase().indexOf(criterio.toUpperCase());
	        		    String buscar= "";
	        		    if (intIndex != - 1) {
	        		    	buscar = "Palabra encontrada , posicion " + intIndex.toString();
	        		    	respulinea.setNombreArchivo(ficheros[x]);
		        		    respulinea.setLugarEncontrado(ruta);
		        		    respulinea.setRespuesta(buscar);
		        		    respulinea.setPathArchivo(mipath);
		        		    //respulinea.setLugarEncontrado("no disponible - demo version");
		        		    
		        		    if (parsedText.length()>101){
		        		    	if (intIndex < 50) {
		        		    		respulinea.setLugarEncontrado(parsedText.substring(intIndex, intIndex + 50));
		        		    	}else{
		        		    		respulinea.setLugarEncontrado(parsedText.substring(1, 100));
		        		    	}	        		    		        		    
		        		    } else {
		        		    	respulinea.setLugarEncontrado(".- " + parsedText);
		        		    	}
		        		    
	        		    	lstrespu.add(respulinea);
	        		    }else {
	        		    	buscar ="Palabra NO encontrada";		    	
	        		    }
	        		   
	        		    
	        		    pdDocument.close();//CERRAMOS OBJETO ACROBAT
	        		    
	        		    
	        		    
	                  } catch (IOException e) {
	                      if(e.toString()!=null){
	                        File archivo=new File("dañado_"+ficheros[x]);//SEPARA LOS DAÑADOS
	                      }
	                      //System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
	                      //e.printStackTrace();
	                  }//CATCH
	                  
	              }//FOR	
	        }//else
		
			//return new ResponseEntity<>(lstrespu, HttpStatus.OK);
			
		} // del for path
		System.out.println("TOTAL ---- " + tot);
		return lstrespu;

	}
	
	
	public List<ModelPdf1> todospdflinux (String criterio) {
		List<String> losPath = new ArrayList<>();
		losPath.add("//unidad//pdf//includes//archivos_preventivo");
		losPath.add("//unidad//pdf//includes//archivos_firmados");
		losPath.add("//unidad//pdf//includes//scan_expedientes");
		losPath.add("//unidad//pdf//sistemaArchivos//cavArchivos");
		losPath.add("//unidad//pdf//sistemaArchivos//racDocumentacion");
		losPath.add("//unidad//pdf//sne//escritosAdjuntos");
		losPath.add("//unidad//pdf//sne//escritosPDF");
		
		List<String> losPathreal = new ArrayList<>();
		losPathreal.add("/home/repositorio/pdf/includes/archivos_preventivo");
		losPathreal.add("/home/repositorio/pdf/archivos_firmados");
		losPathreal.add("/home/repositorio/pdf/scan_expedientes");
		losPathreal.add("/home/repositorio/pdf/sistemaArchivos/cavArchivos");
		losPathreal.add("/home/repositorio/pdf/sistemaArchivos/racDocumentacion");
		losPathreal.add("/home/repositorio/pdf/sne/escritosAdjuntos");
		losPathreal.add("/home/repositorio/pdf/sne/escritosPDF");
		
		
		List<ModelPdf1> lstrespu = new ArrayList<ModelPdf1>();
		int tot = 0;
		for (int i=0; i<losPath.size(); i++) {
					//File dir = new File("c:\\intel\\pdf");
			String mipath = losPath.get(i);
			String pathlinux = losPathreal.get(i);
			File dir = new File(mipath);
			String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
			
	        if (ficheros == null)//EXCEPCION
	              System.out.println("No hay archivos en la carpeta especificada");
	        else {
	            for (int x=0;x<ficheros.length;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
	                String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
	                String rutaLinux = new String();
	                //ruta=("c:\\intel\\pdf\\"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
	                ruta=(mipath + "\\"+ficheros[x]);
	                rutaLinux=(pathlinux + "/" + pathlinux);
	                //System.out.println(tot + " - " + x + " .- " + ruta);
	                tot++;
	                  try {
	                	ModelPdf1 respulinea = new ModelPdf1();
	                	PDDocument pdDocument = null;
	                	pdDocument = PDDocument.load(new File(ruta)); //CARGAR EL PDF
	                     
	                     // List l = pd.getDocumentCatalog().getAllPages();//NUMERO LAS PAGINAS DEL ARCHIVO
	          		    PDFTextStripper pdfStripper = new PDFTextStripper();
	        		    pdfStripper.setStartPage(1);
	        		    pdfStripper.setEndPage(5);
	        		    String parsedText = pdfStripper.getText(pdDocument);
	        		    Integer intIndex = parsedText.toUpperCase().indexOf(criterio.toUpperCase());
	        		    String buscar= "";
	        		    if (intIndex != - 1) {
	        		    	buscar = "Palabra encontrada , posicion " + intIndex.toString();
	        		    	respulinea.setNombreArchivo(ficheros[x]);
		        		    respulinea.setPathArchivo(ruta);
		        		    respulinea.setRespuesta(buscar);
		        		    respulinea.setPathArchivo(mipath);
		        		    respulinea.setPathlinux(rutaLinux);
		        		    //respulinea.setLugarEncontrado("no disponible - demo version");
		        		    
		        		    if (parsedText.length()>101){
		        		    	if (intIndex < 50) {
		        		    		respulinea.setLugarEncontrado(parsedText.substring(intIndex, intIndex + 50));
		        		    	}else{
		        		    		respulinea.setLugarEncontrado(parsedText.substring(1, 100));
		        		    	}	        		    		        		    
		        		    } else {
		        		    	respulinea.setLugarEncontrado(".- " + parsedText);
		        		    	}
		        		    
	        		    	lstrespu.add(respulinea);
	        		    }else {
	        		    	buscar ="Palabra NO encontrada";		    	
	        		    }
	        		   
	        		    
	        		    pdDocument.close();//CERRAMOS OBJETO ACROBAT
	        		    
	        		    
	        		    
	                  } catch (IOException e) {
	                      if(e.toString()!=null){
	                        File archivo=new File("dañado_"+ficheros[x]);//SEPARA LOS DAÑADOS
	                      }
	                      //System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
	                      //e.printStackTrace();
	                  }//CATCH
	                  
	              }//FOR	
	        }//else
		
			//return new ResponseEntity<>(lstrespu, HttpStatus.OK);
			
		} // del for path
		System.out.println("TOTAL ---- " + tot);
		return lstrespu;

	}
	
}
