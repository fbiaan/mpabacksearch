package com.distribuidora.searchengine.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.distribuidora.searchengine.dto.ModelPdf1;

@CrossOrigin("*")
@RestController
@RequestMapping("/pdf")
public class PdfController {

	@GetMapping("/leer")
	public String leerpdf() {
		PDDocument pdDocument = null;
		try {
		    //pdDocument = PDDocument.load(new File("C:/intel/pdf/Organizacion del Equipo de trabajo.pdf"));
			//pdDocument = PDDocument.load(new File("C:/intel/pdf/res-640ba1b5ae831.pdf"));
			pdDocument = PDDocument.load(new File("C:/intel/pdf/5e970364dd7f1-atencion sequeiro tejerina.pdf"));
			
		    PDFTextStripper pdfStripper = new PDFTextStripper();
		    pdfStripper.setStartPage(1);
		    pdfStripper.setEndPage(5);
		    String parsedText = pdfStripper.getText(pdDocument);
		 
		    System.out.println(parsedText);
		    return parsedText;
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
	
	@GetMapping("/leerandsearch/{criterio}")
	public String leerpdf(@PathVariable String criterio) {
		PDDocument pdDocument = null;
		try {
		   // pdDocument = PDDocument.load(new File("C:/intel/pdf/Organizacion del Equipo de trabajo.pdf"));
		    //pdDocument = PDDocument.load(new File("C:/intel/pdf/res-640ba1b5ae831.pdf"));
			pdDocument = PDDocument.load(new File("C:/intel/pdf/dictamen 362-10-05-18 SJ-14321-17.pdf"));
			//pdDocument = PDDocument.load(new File("C:/intel/pdf/dictamen 177-PE-13112.pdf"));
		    PDFTextStripper pdfStripper = new PDFTextStripper();
		    pdfStripper.setStartPage(1);
		    pdfStripper.setEndPage(15);
		    String parsedText = pdfStripper.getText(pdDocument);
		    Integer intIndex = parsedText.toUpperCase().indexOf(criterio.toUpperCase());
		    String buscar= "";
		    if (intIndex != - 1) {
		    	buscar = "Palabra encontrada , posicion " + intIndex.toString();
		    }else {
		    	buscar ="Palabra NO encontrada";		    	
		    }
		    System.out.println(parsedText + "/r/n" + buscar);
		    return parsedText + "/r/n" + buscar;
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

	@GetMapping("/folder/{criterio}")
	public ResponseEntity<?> BuscarenFolder(@PathVariable String criterio) {
		//File dir = new File("c:\\intel\\pdf");
		//File dir = new File("C:\\pdf\\includes\\archivos_preventivo");
		File dir = new File("//tmp");
		String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
		List<ModelPdf1> lstrespu = new ArrayList<ModelPdf1>();
        if (ficheros == null)//EXCEPCION
              System.out.println("No hay archivos en la carpeta especificada");
        else {
            for (int x=0;x<ficheros.length;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
                String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
                //ruta=("c:\\intel\\pdf\\"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
                //ruta=("C:\\pdf\\includes\\archivos_preventivo\\"+ficheros[x]);
                ruta=("//tmp//"+ficheros[x]);
                System.out.println(x + " .- " + ruta);
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
                      System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
                      e.printStackTrace();
                  }//CATCH
                  
              }//FOR	
        }
	
		return new ResponseEntity<>(lstrespu, HttpStatus.OK);
	}
	
	
	@GetMapping("/folderlin/{criterio}")
	public ResponseEntity<?> BuscarenFolderLinux(@PathVariable String criterio) {
		//File dir = new File("c:\\intel\\pdf");
		//File dir = new File("C:\\pdf\\includes\\archivos_preventivo");
		File dir = new File("//unidad");

		String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
		List<ModelPdf1> lstrespu = new ArrayList<ModelPdf1>();
        if (ficheros == null)//EXCEPCION
              System.out.println("No hay archivos en la carpeta especificada");
        else {
            for (int x=0;x<ficheros.length;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
                String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
                //ruta=("c:\\intel\\pdf\\"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
                //ruta=("C:\\pdf\\includes\\archivos_preventivo\\"+ficheros[x]);
                ruta=("//unidad//"+ficheros[x]);
                System.out.println(x + " .- " + ruta);
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
                      System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
                      e.printStackTrace();
                  }//CATCH
                  
              }//FOR	
        }
	
		return new ResponseEntity<>(lstrespu, HttpStatus.OK);
	}
	
	@GetMapping("/folderlinbarra/{criterio}")
	public ResponseEntity<?> BuscarenFolderLinuxbarra(@PathVariable String criterio) {
		//File dir = new File("c:\\intel\\pdf");
		//File dir = new File("C:\\pdf\\includes\\archivos_preventivo");
		File dir = new File("//unidad//pdf");
		
		String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
		List<ModelPdf1> lstrespu = new ArrayList<ModelPdf1>();
        if (ficheros == null)//EXCEPCION
              System.out.println("No hay archivos en la carpeta especificada");
        else {
            for (int x=0;x<ficheros.length;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
                String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
                //ruta=("c:\\intel\\pdf\\"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
                //ruta=("C:\\pdf\\includes\\archivos_preventivo\\"+ficheros[x]);
                ruta=("//unidad//pdf//"+ficheros[x]);
                System.out.println(x + " .- " + ruta);
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
                      System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
                      e.printStackTrace();
                  }//CATCH
                  
              }//FOR	
        }
	
		return new ResponseEntity<>(lstrespu, HttpStatus.OK);
	}
	
	
	@GetMapping("/folderss/{criterio}")
	public ResponseEntity<?> BuscarVariosFolder(@PathVariable String criterio) {
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
	                System.out.println(tot + " - " + x + " .- " + ruta);
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
	                      System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
	                      e.printStackTrace();
	                  }//CATCH
	                  
	              }//FOR	
	        }//else
		
			//return new ResponseEntity<>(lstrespu, HttpStatus.OK);
			
		} // del for path 	
		return new ResponseEntity<>(lstrespu, HttpStatus.OK);
	} 
	
	
}
