package com.cwang.util;

import javax.script.*;    
import org.python.core.Py;
import org.python.core.PyFunction;  
import org.python.core.PyInteger;  
import org.python.core.PyObject;  
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;  
import org.python.core.PyList;
import org.python.core.PyString;
  
import java.io.*;  
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;  
public class PythonLog  
{  
	private static String[][] data = {{"A1", "A2","A3"},{"B1", "B2","B3"},{"C1", "C2","C3"}};
	private static String filename = "G:\\pyworkspace\\Util\\LogUtil\\LogFile\\2016-06-07.xlsx";
	
    public static void main(String args[]) {      	
    	jython(data, filename);
    	
    	//runtimeExec(data, filename);
  
    	
    	
    } 
    
    /**
     * 使用Runtime执行python脚本
     * @param data		日志数据
     * @param filename	日志文件全名
     */
    public static void runtimeExec(String[][] data, String filename){
    	try{   		        	
        	String cmdData = "[";
        	for(String[] row : data){
        		cmdData += "[";
        		for(String cell : row){
        			cmdData += "\'"+ cell + "\'" + ",";
        		}
        		cmdData = cmdData.substring(0, cmdData.length()-1);    		
        		cmdData += "],";
        		
        	}
        	cmdData = cmdData.substring(0, cmdData.length()-1);
        	cmdData += "]";        	        
        	        	        	
        	String[] command = {"python","G:\\pyworkspace\\Util\\LogUtil\\LogToExcel.py", cmdData, filename};
        	
        	Process pr = Runtime.getRuntime().exec(command);
        	int exitcode = pr.waitFor();
        	
        	if(exitcode == 0){
        		System.out.println("Success");
        	}else{
        		System.out.println("Failed");
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    /**
     * 使用jython在java程序中调用python脚本，需要JDK1.7及以上版本
     * @param data		日志数据
     * @param filename	日志文件全名
     */
    public static void jython(String[][] data, String filename){
    	try{
    		PythonInterpreter interpreter = new PythonInterpreter();  
        	
            interpreter.execfile("G:\\pyworkspace\\Util\\LogUtil\\LogToExcel.py");  
            PyFunction func = (PyFunction)interpreter.get("log_excel",PyFunction.class);                   
            
            PyList datalist = new PyList();
            for(String[] row : data){
            	PyList onerow = new PyList();
            	for(String cell : row){
            		onerow.add(new PyString(cell));
            	}
            	datalist.add(onerow);
            }
            
            PyString pyfilename = new PyString(filename);        
            
            PyObject pyobj = func.__call__(datalist, pyfilename);
            
            System.out.println("result = " + pyobj.toString()); 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
}  