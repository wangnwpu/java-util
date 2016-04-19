package com.cwang.util;
/**
 * 线程控制
 * 通过外部输入，控制线程暂停和恢复
 */


import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class ControlThread extends Thread {  
	
	private static int counter = 0;
	
	private static int runcount = 0;
	
    private boolean suspend = false;  
  
    private String control = ""; // 只是需要一个对象而已，这个对象没有实际意义  
  
    public void setSuspend(boolean suspend) {  
        if (!suspend) {  
            synchronized (control) {  
                control.notifyAll();  
            }  
        }  
        this.suspend = suspend;  
    }  
  
    public boolean isSuspend() {  
        return this.suspend;  
    }  
  
    public void run() {  
        //while (true) {  
            synchronized (control) {  
                if (suspend) {  
                    try {  
                        control.wait();  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
            this.runPersonelLogic(); 
            runcount++;
            System.out.println("Runcount: " + runcount);
        //}  
    }  
  
    protected abstract void runPersonelLogic();  
      
    public static void main(String[] args) throws Exception {  
    	ControlThread myThread = new ControlThread() {  
            protected void runPersonelLogic() {
            	try{
            		for(int i = 0; i < 10; i++){
                		counter++;
                        System.out.println("counter: " + counter);  
                	}
                	Thread.sleep(1000);
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            }  
        };
        
        myThread.start();
        
        System.out.println("输入 s 暂停, 输入 c 继续");  
        
        while(true){
        	BufferedReader strin=new BufferedReader(new InputStreamReader(System.in)); 
        				
            String str = strin.readLine();  
            
            if(str.equals("s")){
            	myThread.setSuspend(true);  
            }else if(str.equals("c")){
            	 myThread.setSuspend(false);            
            }                            
        }
       
    }  
}  
